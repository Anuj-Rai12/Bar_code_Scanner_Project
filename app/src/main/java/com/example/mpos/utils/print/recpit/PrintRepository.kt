package com.example.mpos.utils.print.recpit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.example.mpos.data.billing.printInvoice.json.*
import com.example.mpos.data.confirmOrder.response.json.ItemList
import com.example.mpos.data.confirmOrder.response.json.PrintReceiptInfo
import com.example.mpos.data.printkot.json.PrintKotInvoice
import com.example.mpos.payment.pine.AppConfig
import com.example.mpos.payment.pine.request.Datum
import com.example.mpos.payment.qr.CreateQr
import com.example.mpos.payment.unit.ImageConvertor
import com.example.mpos.payment.unit.trimBorders
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.Rs_Symbol
import com.example.mpos.utils.getEmojiByUnicode
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.ByteArrayOutputStream

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

    private val header = "${createNewString("Description", 25)}${
        createNewString(
            "Qty",
            7
        )
    }" + "${createNewString("Price", 8)}${createNewString("Amount", 10)}\n"

    private val headerPine = "${createNewString("Description", 15)}${
        createNewString(
            "Qty",
            4
        )
    }" + "${createNewString("Price", 6)}${createNewString("Amount", 6)}"

    private val headerGst = "${createNewString("GST %", 25)}${createNewString("CGST", 7)}" + "${
        createNewString(
            "SGST",
            8
        )
    }${createNewString("CESS", 10)}\n"

    private val headerGstPine = "${createNewString("GST %", 8)}${createNewString("CGST", 8)}" + "${
        createNewString(
            "SGST",
            8
        )
    }${createNewString("CESS", 8)}"


    private val headerVATPine = "${createNewString("VAT/ST", 10)}${
        createNewString(
            "Base Amt",
            10
        )
    }" + createNewString("VAT/ST AMT", 10)


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


    fun doPrint(responseBody: PrintReceiptInfo, times: Int) = flow {
        emit(ApisResponse.Loading("Please Wait Printing Receipt ${getEmojiByUnicode(0x1F5A8)}"))

        val data = try {
            val connection: BluetoothConnection? = BluetoothPrintersConnections.selectFirstPaired()
            connection?.let {
                val printer = EscPosPrinter(connection, 203, 80f, 32)
                val stringBuilder = StringBuilder()
                Log.i("PRINT_ANUJ", "doPrint: ${responseBody.itemList}")
                stringBuilder.append(
                    "[C]$underLine" + "[L][C]${responseBody.headerTxt}\n" + "[L][C]" + "${responseBody.headerTxt2}\n" + "[C]$line" + "[L] Order No .: " + "[R]${responseBody.orderId}  " + "[R]" + responseBody.datetime + "\n" + "[C]$line" + "[L]" + header + "[C]$line" + setTable(
                        responseBody.itemList
                    ) + "[C]$line" + "[L]" + createNewString("Total Amt Excel. Of GST", 40) + "${
                        createNewString(
                            ListOfFoodItemToSearchAdaptor.setPrice(responseBody.amtExclGST)
                                .toString(), 10
                        )
                    }\n" + "[C]$line" + "[L]" + "${
                        createNewString(
                            "No. of Items ", 40
                        )
                    }${
                        createNewString(
                            responseBody.itemList.size.toString(),
                            10
                        )
                    }\n" + "[C]$line" + "[L]" + "${
                        createNewString(
                            "Total Amt Inc. Of GST", 40
                        )
                    }${
                        createNewString(
                            ListOfFoodItemToSearchAdaptor.setPrice(responseBody.amtInclGST)
                                .toString(), 10
                        )
                    }\n" + "[C]$line" + "[L]" + "${createNewString("Total", 40)}${
                        createNewString(
                            ListOfFoodItemToSearchAdaptor.setPrice(responseBody.amtInclGST)
                                .toString(), 10
                        )
                    }\n" + "[C]$underLine"
                )
//"[C]<barcode type='128' width='40' text='above'>${responseBody.orderId}</barcode>\n"+
                printer.printFormattedText(stringBuilder.toString())
                printer.disconnectPrinter()
                return@let if (times < 2) {
                    ApisResponse.Success(Pair(responseBody, times + 1))
                } else {
                    ApisResponse.Success("Receipt Printed ${getEmojiByUnicode(0x1F5A8)}")
                }
            } ?: ApisResponse.Error("Please connect to Printer", null)
        } catch (e: Exception) {
            Log.i("PRINT_ANUJ", "doPrint: ${e.localizedMessage}")
            setCashAnalytics(e)
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


    fun doPineLabPrintInvoice(responseBody: PrintInvoice) = flow {
        emit(ApisResponse.Loading("Please Wait Printing Invoice ${getEmojiByUnicode(0x1F5A8)}"))
        val data = try {
            val arr = ArrayList<Datum>()
            arr.add(setPineLabPrintData(responseBody.headerTxt1))
            arr.add(setPineLabPrintData(responseBody.headerTxt2))
            arr.add(setPineLabPrintData(responseBody.headerTxt3))
            arr.add(setPineLabPrintData(responseBody.headerTxt4))
            arr.add(setPineLabPrintData(responseBody.headerTxt5))
            arr.add(setPineLabPrintData(responseBody.headerTxt6))
            arr.add(setPineLabPrintData(responseBody.headerTxt7))
            arr.add(line())
            arr.add(setPineLabPrintData(responseBody.billType))
            arr.add(line())
            arr.add(setPineLabPrintData(responseBody.subHeaderTxt1))
            arr.add(setPineLabPrintData(responseBody.subHeaderTxt2))
            arr.add(setPineLabPrintData(responseBody.subHeaderTxt3))
            arr.add(setPineLabPrintData(responseBody.subHeaderTxt4))
            arr.add(setPineLabPrintData(responseBody.subHeaderTxt5))
            arr.add(line())
            arr.add(setPineLabPrintData(headerPine, false))
            arr.add(line())
            arr.add(
                setPineLabPrintData(
                    setBillInvoiceTable(
                        responseBody.childitemList, descSize = 14, qty = 2, price = 8, amt = 8
                    ), false
                )
            )
            arr.add(line())
            arr.add(setPineLabPrintData(headerGstPine, false))
            arr.add(line())
            arr.add(
                setPineLabPrintData(
                    setGstTable(
                        responseBody.gstDetails, descSize = 8, qty = 8, price = 8, amt = 8
                    ), false
                )
            )
            arr.add(line())
            arr.add(setPineLabPrintData(headerVATPine, false))
            arr.add(line())
            arr.add(
                setPineLabPrintData(
                    setVATable(
                        responseBody.vatDetails
                    ), false
                )
            )
            arr.add(line())
            arr.add(
                setPineLabPrintData(
                    "Amount Including GST $Rs_Symbol ${responseBody.amtIncGST}", false
                )
            )
            arr.add(line())
            arr.add(setPineLabPrintData("Rounding Amt $Rs_Symbol ${responseBody.roundAmt}", false))
            arr.add(line())
            arr.add(
                setPineLabPrintData(
                    "Rounding Total $Rs_Symbol ${
                        getRoundingTotal(
                            responseBody.amtIncGST, responseBody.roundAmt
                        )
                    }", false
                )
            )
            arr.add(line())
            arr.add(
                setPineLabPrintData(
                    setTenderTable(
                        responseBody.paymentDetails, tenderSize = 16, amtSize = 16
                    ), false
                )
            )
            arr.add(line())
//Qr
            val qr = CreateQr()
            val bitmap = qr.createQr("${responseBody.qrPrint}")
            bitmap?.let { bit ->
                val buffer = qr.bitInputStream(bit.trimBorders(Color.WHITE))
                val bitmap1 = BitmapFactory.decodeStream(buffer)
                val resizedBitmap = Bitmap.createScaledBitmap(
                    bitmap1, 150, 150, false
                )
                    .trimBorders(Color.WHITE) //else Bitmap1.createScaledBitmap(inputBitmap, 184, 35, false)*/
                val output = ByteArrayOutputStream(resizedBitmap.byteCount)
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
                val imageBytes = output.toByteArray()
                val imgData = ImageConvertor.bytesToHex(imageBytes)
                val datum = Datum()
                datum.printDataType = "2"
                datum.printerWidth = AppConfig.PrinterWidth
                datum.isCenterAligned = true
                datum.dataToPrint = ""
                datum.imagePath = ""
                datum.imageData = imgData
                arr.add(datum)
                arr.add(line())
            }
            arr.add(setPineLabPrintData(responseBody.footerTxt1))
            arr.add(setPineLabPrintData(responseBody.footerTxt2))
            arr.add(setPineLabPrintData(responseBody.footerTxt3))
            arr.add(setPineLabPrintData(responseBody.footerTxt4))
            arr.add(setPineLabPrintData(responseBody.footerTxt5))
            arr.add(setPineLabPrintData(responseBody.footerTxt6))
            arr.add(line())
            ApisResponse.Success(arr)
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)

    fun doPineLabPrintKOTInvoice(responseBody: PrintKotInvoice) = flow {
        emit(ApisResponse.Loading("Please Wait Printing KOT Invoice ${getEmojiByUnicode(0x1F5A8)}"))
        val data = try {
            val item = mutableListOf<Childitem>()
            responseBody.childitemList.forEach { res ->
                item.add(
                    Childitem(
                        amount = res.amount,
                        description = res.description,
                        itemcode = res.itemcode,
                        price = res.price,
                        qty = res.qty.toInt()
                    )
                )
            }
            val arr = ArrayList<Datum>()
            arr.add(setPineLabPrintData(responseBody.headerTxt1))
            arr.add(setPineLabPrintData(responseBody.headerTxt2))
            arr.add(setPineLabPrintData(responseBody.headerTxt3))
            arr.add(setPineLabPrintData(responseBody.headerTxt4))
            arr.add(setPineLabPrintData(responseBody.headerTxt5))
            arr.add(setPineLabPrintData(responseBody.headerTxt6))
            arr.add(setPineLabPrintData(responseBody.headerTxt7))
            arr.add(line())
            arr.add(setPineLabPrintData(responseBody.billType))
            arr.add(line())
            arr.add(setPineLabPrintData(responseBody.subHeaderTxt1))
            arr.add(setPineLabPrintData(responseBody.subHeaderTxt2))
            arr.add(setPineLabPrintData(responseBody.subHeaderTxt3))
            arr.add(setPineLabPrintData(responseBody.subHeaderTxt4))
            arr.add(setPineLabPrintData(responseBody.subHeaderTxt5))
            arr.add(line())
            arr.add(setPineLabPrintData(headerPine, false))
            arr.add(line())
            arr.add(
                setPineLabPrintData(
                    setBillInvoiceTable(
                        item, descSize = 14, qty = 2, price = 8, amt = 8
                    ), false
                )
            )
            arr.add(line())
            arr.add(
                setPineLabPrintData(
                    "Base Amount $Rs_Symbol ${responseBody.baseAmount}",
                    false
                )
            )
            arr.add(line())
            ApisResponse.Success(arr)
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)

    }.flowOn(IO)

    private fun setPineLabPrintData(msg: String, isCenterAlign: Boolean = true): Datum {
        val datum = Datum()
        datum.imageData = "0"
        datum.dataToPrint = msg
        datum.imagePath = "0"
        datum.printDataType = "0"
        datum.printerWidth = AppConfig.PrinterWidth
        datum.isCenterAligned = isCenterAlign
        return datum
    }

    private fun line(): Datum {
        val datum = Datum()
        datum.imageData = "0"
        datum.dataToPrint = "--------------------------------"
        datum.imagePath = "0"
        datum.printDataType = "0"
        datum.isCenterAligned = true
        datum.printerWidth = AppConfig.PrinterWidth
        return datum
    }

    fun doPrintInvoice(responseBody: PrintInvoice) = flow {
        emit(ApisResponse.Loading("Please Wait Printing Invoice ${getEmojiByUnicode(0x1F5A8)}"))
        val data = try {
            val connection: BluetoothConnection? = BluetoothPrintersConnections.selectFirstPaired()
            connection?.let {
                val printer = EscPosPrinter(connection, 203, 80f, 32)
                val stringBuilder = StringBuilder()
                val qrTxt = getScanQr(responseBody.qrPrint)
                stringBuilder.append(
                    "[C]$underLine" + "[L][C]${responseBody.headerTxt1}\n" + "[L][C]" + "${responseBody.headerTxt2}\n" + "[L][C]" + "${responseBody.headerTxt3}\n" + "[L][C]" + "${responseBody.headerTxt4}\n" + "[L][C]" + "${responseBody.headerTxt5}\n" + "[L][C]" + "${responseBody.headerTxt6}\n" + "[L][C]" + "${responseBody.headerTxt7}\n" + "[C]$doubleLine" + "[L][C]" + "${responseBody.billTypeTxt}\n" + "[C]$doubleLine" + "[L][C]" + "${responseBody.billType}\n" + "[C]$doubleLine" + "[L] ${responseBody.subHeaderTxt1}" + "\n" + "[L] ${responseBody.subHeaderTxt2}" + "\n" + "[L] ${responseBody.subHeaderTxt3}" + "\n" + "[L] ${responseBody.subHeaderTxt4}" + "\n" + "[L] ${responseBody.subHeaderTxt5}" + "\n" + "[C]$doubleLine" + "[L]" + header + "[C]$doubleLine" + setBillInvoiceTable(
                        responseBody.childitemList
                    ) + "[C]$doubleLine" + "[L]" + createNewString(
                        "Total",
                        40
                    ) + "${
                        createNewString(
                            responseBody.baseAmount,
                            10
                        )
                    }\n" + "[C]$doubleLine" + "[L]" + headerGst + "[C]$doubleLine" + setGstTable(
                        responseBody.gstDetails
                    ) + "[C]$doubleLine" + "[L]" + createNewString(
                        "Amount Including GST",
                        40
                    ) + "${
                        createNewString(
                            responseBody.amtIncGST,
                            10
                        )
                    }\n" + "[C]$doubleLine" + "[L]" + createNewString(
                        "Rounding Amt",
                        40
                    ) + "${
                        createNewString(
                            responseBody.roundAmt,
                            10
                        )
                    }\n" + "[C]$doubleLine" + "[L]" + createNewString("Rounding Total", 40) + "${
                        createNewString(
                            getRoundingTotal(
                                responseBody.amtIncGST, responseBody.roundAmt
                            ), 10
                        )
                    }\n" + "[C]$doubleLine" + setTenderTable(responseBody.paymentDetails) + "[C]$doubleLine" + "[L]${responseBody.footerTxt1}\n" + "[L]${responseBody.footerTxt2}\n" + "[L]${responseBody.footerTxt3}\n" + "[L]${responseBody.footerTxt4}\n" + "[L]${responseBody.footerTxt5}\n" + "[L]${responseBody.footerTxt6}\n" + "[L]${responseBody.footerTxt7}\n" + "[C]<qrcode size='40'>$qrTxt</qrcode>\n" +
                            //<qrcode size='30'></qrcode>" +
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

    private fun getScanQr(qrPrint: List<QrPrint>): String {
        val stringBuilder = StringBuilder()
        qrPrint.forEach {
            stringBuilder.append(it.tenderType)
            stringBuilder.append("\n")
            stringBuilder.append(it.amt)
            stringBuilder.append("\n$line")
        }
        return stringBuilder.toString()
    }

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
            val value = "[L]${createNewString(item.description, 25)}" + createNewString(
                "${item.qty}",
                7
            ) + createNewString(
                ListOfFoodItemToSearchAdaptor.setPrice(item.price).toString(), 8
            ) + "${
                createNewString(
                    ListOfFoodItemToSearchAdaptor.setPrice(item.amount).toString(), 10
                )
            }\n"
            stringBuilder.append(value)
        }
        return stringBuilder.toString()
    }


    private fun setBillInvoiceTable(
        list: List<Childitem>, descSize: Int = 25, qty: Int = 7, price: Int = 8, amt: Int = 10
    ): String {
        val stringBuilder = StringBuilder()
        list.forEach { item ->
            val value = "${if (descSize == 25) "[L]" else ""}${
                createNewString(
                    item.description, descSize
                )
            }${
                createNewString(
                    item.qty.toString(), qty
                )
            }" + createNewString(
                ListOfFoodItemToSearchAdaptor.setPrice(item.price).toString(), price
            ) + "${
                createNewString(
                    ListOfFoodItemToSearchAdaptor.setPrice(item.amount).toString(), amt
                )
            }${if (descSize == 25) "\n" else ""}"
            stringBuilder.append(value)
        }
        return stringBuilder.toString()
    }

    private fun setGstTable(
        list: List<GstDetail>, descSize: Int = 25, qty: Int = 7, price: Int = 8, amt: Int = 10
    ): String {
        val stringBuilder = StringBuilder()
        list.forEach { item ->
            val value =
                "${if (descSize == 25) "[L]" else ""}${createNewString(item.gstPer, descSize)}${
                    createNewString(
                        item.cGSTAmt, qty
                    )
                }" + createNewString(item.sGSTAmt, price) + "${
                    createNewString(
                        item.cessAmt,
                        amt
                    )
                }${if (descSize == 25) "\n" else ""}"
            stringBuilder.append(value)
        }
        return stringBuilder.toString()
    }


    private fun setVATable(
        list: List<VatDetail>, percentage: Int = 10, baseAmt: Int = 10, vatAmt: Int = 10
    ): String {
        val stringBuilder = StringBuilder()
        list.forEach { item ->
            val value = "${createNewString(getDigitOnlyValue(item.vatPer), percentage)}${
                createNewString(
                    item.vatBaseAmt,
                    baseAmt
                )
            }" +
                    createNewString(item.vatAmt, vatAmt)
            stringBuilder.append(value)
        }
        return stringBuilder.toString()
    }

    private fun getDigitOnlyValue(string: String, addon: String = "%"): String {
        return string.filter { it.isDigit() || it == '.' } + addon
    }


    private fun setTenderTable(
        list: List<PaymentDetail>, tenderSize: Int = 40, amtSize: Int = 10
    ): String {
        val stringBuilder = StringBuilder()
        list.forEach { item ->
            val value = (if (tenderSize == 40) "[L]" else "") + createNewString(
                item.tenderType, tenderSize
            ) + "${createNewString(item.amt, amtSize)}${if (tenderSize == 25) "\n" else ""}"
            stringBuilder.append(value)
        }
        return stringBuilder.toString()
    }

}