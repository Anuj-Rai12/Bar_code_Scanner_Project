package com.example.mpos.utils.print.recpit

import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.example.mpos.data.billing.printInvoice.json.Childitem
import com.example.mpos.data.billing.printInvoice.json.GstDetail
import com.example.mpos.data.billing.printInvoice.json.PaymentDetail
import com.example.mpos.data.billing.printInvoice.json.PrintInvoice
import com.example.mpos.data.confirmOrder.response.json.ItemList
import com.example.mpos.data.confirmOrder.response.json.PrintReceiptInfo
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.getEmojiByUnicode
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PrintRepository {

    companion object {
        fun setCashAnalytics(e: Exception) {
            Firebase.crashlytics.recordException(e)
        }
    }

    private val printNotConnectedToProceed =
        "Cannot find the Printer${getEmojiByUnicode(0x1F5A8)} to Print Bill\nAre you sure you want Proceed without it?\n"


    private val line = "------------------------------------------------\n"
    private val doubleLine = "================================================\n"

    private val underLine = "<u>                                                </u>\n"

    private val header = "${createNewString("Description", 25)}${createNewString("Qty", 7)}" +
            "${createNewString("Price", 8)}${createNewString("Amount", 10)}\n"

    private val headerGst = "${createNewString("GST %", 25)}${createNewString("CGST", 7)}" +
            "${createNewString("SGST", 8)}${createNewString("CESS", 10)}\n"

    fun isPrintSelected() = flow {
        emit(ApisResponse.Loading("Checking Printer"))
        try {
            val connection = BluetoothPrintersConnections.selectFirstPaired()
            if (connection != null && connection.isConnected) {
                connection.disconnect()
                emit(ApisResponse.Success("Success"))
            } else {
                emit(ApisResponse.Error(printNotConnectedToProceed, null))
            }
        } catch (e: Exception) {
            setCashAnalytics(e)
            emit(ApisResponse.Error(null, e))
        }
    }.flowOn(IO)


    fun doPrint(responseBody: PrintReceiptInfo) = flow {
        emit(ApisResponse.Loading("Please Wait Printing Receipt ${getEmojiByUnicode(0x1F5A8)}"))

        val data = try {
            val connection: BluetoothConnection? = BluetoothPrintersConnections.selectFirstPaired()
            connection?.let {
                val printer = EscPosPrinter(connection, 203, 80f, 32)
                val stringBuilder = StringBuilder()
                Log.i("PRINT_ANUJ", "doPrint: ${responseBody.itemList}")
                stringBuilder.append(
                    "[C]$underLine" +
                            "[L][C]${responseBody.headerTxt}\n" +
                            "[L][C]" + "${responseBody.headerTxt2}\n" +
                            "[C]$line" +
                            "[L] Order No .: " + "[R]${responseBody.orderId}  " + "[R]" + responseBody.datetime + "\n" +
                            "[C]$line" +
                            "[L]" + header +
                            "[C]$line" +
                            setTable(responseBody.itemList) +
                            "[C]$line" +
                            "[L]" +
                            createNewString("Total Amt Excel. Of GST", 40) +
                            "${createNewString(ListOfFoodItemToSearchAdaptor.setPrice(responseBody.amtExclGST).toString(), 10)}\n" +
                            "[C]$line" +
                            "[L]" + "${
                        createNewString(
                            "No. of Items ",
                            40
                        )
                    }${createNewString(responseBody.itemList.size.toString(), 10)}\n" +
                            "[C]$line" +
                            "[L]" + "${
                        createNewString(
                            "Total Amt Inc. Of GST",
                            40
                        )
                    }${
                        createNewString(
                            ListOfFoodItemToSearchAdaptor.setPrice(responseBody.amtInclGST)
                                .toString(), 10
                        )
                    }\n" +
                            "[C]$line" +
                            "[L]" + "${createNewString("Total", 40)}${
                        createNewString(
                            ListOfFoodItemToSearchAdaptor.setPrice(responseBody.amtInclGST)
                                .toString(),
                            10
                        )
                    }\n" +
                            "[C]$underLine"
                )

                printer.printFormattedText(stringBuilder.toString())
                printer.disconnectPrinter()
                return@let ApisResponse.Success("Receipt Printed ${getEmojiByUnicode(0x1F5A8)}")
            } ?: ApisResponse.Error("Please connect to Printer", null)
        } catch (e: Exception) {
            Log.i("PRINT_ANUJ", "doPrint: ${e.localizedMessage}")
            setCashAnalytics(e)
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


    /*fun deleteFun(responseBody: PrintInvoice) = flow {
        emit(ApisResponse.Loading("Please Wait Printing Invoice ${getEmojiByUnicode(0x1F5A8)}"))
        val data = try {
            val stringBuilder = StringBuilder()
            stringBuilder.append(
                "[C]$line" +
                        "[L][C]${responseBody.headerTxt1}\n" +
                        "[L][C]" + "${responseBody.headerTxt2}\n" +
                        "[L][C]" + "${responseBody.headerTxt3}\n" +
                        "[L][C]" + "${responseBody.headerTxt4}\n" +
                        "[L][C]" + "${responseBody.headerTxt5}\n" +
                        "[L][C]" + "${responseBody.headerTxt6}\n" +
                        "[L][C]" + "${responseBody.headerTxt7}\n" +
                        "[C]$line" +
                        "[L][C]" + "${responseBody.billTypeTxt}\n" +
                        "[C]$line" +
                        "[L][C]" + "${responseBody.billType}\n" +
                        "[C]$line" +
                        "[L][C] ${responseBody.subHeaderTxt1}" + "\n" +
                        "[L][C] ${responseBody.subHeaderTxt2}" + "\n" +
                        "[L][C] ${responseBody.subHeaderTxt3}" + "\n" +
                        "[L][C] ${responseBody.subHeaderTxt4}" + "\n" +
                        "[L][C] ${responseBody.subHeaderTxt5}" + "\n" +
                        "[C]$line" +
                        "[L]" + header +
                        "[C]$line" +
                        setBillInvoiceTable(responseBody.childitemList) +
                        "[C]$line" +
                        "[L]" + createNewString("Total", 40) +
                        "${createNewString(responseBody.baseAmount, 10)}\n" +
                        "[C]$line" +
                        "[L]" + headerGst +
                        "[C]$line" +
                        setGstTable(responseBody.gstDetails) +
                        "[C]$line" +
                        "[L]" + createNewString("Amount Including GST", 40) +
                        "${createNewString(responseBody.amtIncGST, 10)}\n" +
                        "[C]$line" +
                        "[L]" + createNewString("Rounding Amt", 40) +
                        "${createNewString(responseBody.roundAmt, 10)}\n" +
                        "[C]$line" +
                        "[L]" + createNewString("Rounding Total", 40) +
                        "${createNewString(getRoundingTotal(responseBody.amtIncGST, responseBody.roundAmt), 10)}\n" +
                        "[C]$line" +
                        setTenderTable(responseBody.paymentDetails) +
                        "[C]$line" +
                        "[L][C]${responseBody.footerTxt1}\n" +
                        "[L][C]${responseBody.footerTxt2}\n" +
                        "[L][C]${responseBody.footerTxt3}\n" +
                        "[L][C]${responseBody.footerTxt4}\n" +
                        "[L][C]${responseBody.footerTxt5}\n" +
                        "[L][C]${responseBody.footerTxt6}\n" +
                        "[L][C]${responseBody.footerTxt7}\n" +
                        "[C]$line"
            )

            Log.i("PRINT_ANUJ", "deleteFun:success $stringBuilder")
            ApisResponse.Success("Success will do it")
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)*/


    fun doPrintInvoice(responseBody: PrintInvoice) = flow {
        emit(ApisResponse.Loading("Please Wait Printing Invoice ${getEmojiByUnicode(0x1F5A8)}"))
        val data = try {
            val connection: BluetoothConnection? = BluetoothPrintersConnections.selectFirstPaired()
            connection?.let {
                val printer = EscPosPrinter(connection, 203, 80f, 32)
                val stringBuilder = StringBuilder()
                stringBuilder.append(
                    "[C]$underLine" +
                            "[L][C]${responseBody.headerTxt1}\n" +
                            "[L][C]" + "${responseBody.headerTxt2}\n" +
                            "[L][C]" + "${responseBody.headerTxt3}\n" +
                            "[L][C]" + "${responseBody.headerTxt4}\n" +
                            "[L][C]" + "${responseBody.headerTxt5}\n" +
                            "[L][C]" + "${responseBody.headerTxt6}\n" +
                            "[L][C]" + "${responseBody.headerTxt7}\n" +
                            "[C]$doubleLine" +
                            "[L][C]" + "${responseBody.billTypeTxt}\n" +
                            "[C]$doubleLine" +
                            "[L][C]" + "${responseBody.billType}\n" +
                            "[C]$doubleLine" +
                            "[L] ${responseBody.subHeaderTxt1}" + "\n" +
                            "[L] ${responseBody.subHeaderTxt2}" + "\n" +
                            "[L] ${responseBody.subHeaderTxt3}" + "\n" +
                            "[L] ${responseBody.subHeaderTxt4}" + "\n" +
                            "[L] ${responseBody.subHeaderTxt5}" + "\n" +
                            "[C]$doubleLine" +
                            "[L]" + header +
                            "[C]$doubleLine" +
                            setBillInvoiceTable(responseBody.childitemList) +
                            "[C]$doubleLine" +
                            "[L]" + createNewString("Total", 40) +
                            "${createNewString(responseBody.baseAmount, 10)}\n" +
                            "[C]$doubleLine" +
                            "[L]" + headerGst +
                            "[C]$doubleLine" +
                            setGstTable(responseBody.gstDetails) +
                            "[C]$doubleLine" +
                            "[L]" + createNewString("Amount Including GST", 40) +
                            "${createNewString(responseBody.amtIncGST, 10)}\n" +
                            "[C]$doubleLine" +
                            "[L]" + createNewString("Rounding Amt", 40) +
                            "${createNewString(responseBody.roundAmt, 10)}\n" +
                            "[C]$doubleLine" +
                            "[L]" + createNewString("Rounding Total", 40) +
                            "${createNewString(getRoundingTotal(responseBody.amtIncGST, responseBody.roundAmt), 10)}\n" +
                            "[C]$doubleLine" +
                            setTenderTable(responseBody.paymentDetails) +
                            "[C]$doubleLine" +
                            "[L]${responseBody.footerTxt1}\n" +
                            "[L]${responseBody.footerTxt2}\n" +
                            "[L]${responseBody.footerTxt3}\n" +
                            "[L]${responseBody.footerTxt4}\n" +
                            "[L]${responseBody.footerTxt5}\n" +
                            "[L]${responseBody.footerTxt6}\n" +
                            "[L]${responseBody.footerTxt7}\n" +
                            "[C]$underLine"
                )

                printer.printFormattedText(stringBuilder.toString())
                printer.disconnectPrinter()
                return@let ApisResponse.Success("Invoice Printed ${getEmojiByUnicode(0x1F5A8)} Successfully")
            } ?: ApisResponse.Error("Please connect to Printer", null)
        } catch (e: Exception) {
            Log.i("PRINT_ANUJ", "doPrint: ${e.localizedMessage}")
            setCashAnalytics(e)
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)

    private fun getRoundingTotal(amtIncGST: String, roundAmt: String): String {
        val gst = ListOfFoodItemToSearchAdaptor.setPrice(amtIncGST)
        val amt2 = ListOfFoodItemToSearchAdaptor.setPrice(roundAmt)
        val amt = gst + amt2
        return "%.4f".format(amt)
    }


    private fun createNewString(msg: String, size: Int): String {
        val spaceStr = StringBuilder()
        if (msg.length > size) {
            var space = 2
            spaceStr.append(msg.substring(0, size - 2))
            while (space != 0) {
                spaceStr.append(" ")
                space -= 1
            }
            return spaceStr.toString()
        }

        var space = size - msg.length
        spaceStr.append(msg)
        while (space != 0) {
            spaceStr.append(" ")
            space -= 1
        }

        return spaceStr.toString()
    }


    private fun setTable(list: List<ItemList>): String {
        val stringBuilder = StringBuilder()
        list.forEach { item ->
            val value =
                "[L]${createNewString(item.description, 25)}" +
                        createNewString("${item.qty}", 7) + createNewString(ListOfFoodItemToSearchAdaptor.setPrice(item.price).toString(), 8) +
                        "${createNewString(ListOfFoodItemToSearchAdaptor.setPrice(item.amount).toString(), 10)}\n"
            stringBuilder.append(value)
        }
        return stringBuilder.toString()
    }


    private fun setBillInvoiceTable(list: List<Childitem>): String {
        val stringBuilder = StringBuilder()
        list.forEach { item ->
            val value =
                "[L]${createNewString(item.description, 25)}${createNewString(item.qty.toString(), 7)}" +
                        createNewString(ListOfFoodItemToSearchAdaptor.setPrice(item.price).toString(), 8) +
                        "${createNewString(ListOfFoodItemToSearchAdaptor.setPrice(item.amount).toString(), 10)}\n"
            stringBuilder.append(value)
        }
        return stringBuilder.toString()
    }

    private fun setGstTable(list: List<GstDetail>): String {
        val stringBuilder = StringBuilder()
        list.forEach { item ->
            val value =
                "[L]${createNewString(item.gstPer, 25)}${createNewString(item.cGSTAmt, 7)}"+
                        createNewString(item.sGSTAmt, 8) +
                        "${createNewString(item.cessAmt, 10)}\n"
            stringBuilder.append(value)
        }
        return stringBuilder.toString()
    }


    private fun setTenderTable(list: List<PaymentDetail>): String {
        val stringBuilder = StringBuilder()
        list.forEach { item ->
            val value = "[L]" + createNewString(item.tenderType, 40) +
                    "${createNewString(item.amt, 10)}\n"
            stringBuilder.append(value)
        }
        return stringBuilder.toString()
    }

}