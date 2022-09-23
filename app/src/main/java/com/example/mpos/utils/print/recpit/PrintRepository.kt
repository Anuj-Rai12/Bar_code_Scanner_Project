package com.example.mpos.utils.print.recpit

import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.example.mpos.data.confirmOrder.response.json.ItemList
import com.example.mpos.data.confirmOrder.response.json.PrintReceiptInfo
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.getEmojiByUnicode
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PrintRepository {

    private val printNotConnectedToProceed =
        "Cannot find the Printer${getEmojiByUnicode(0x1F5A8)} to Print Bill\nAre you sure you want Proceed without it?\n"


    private val line = "------------------------------------------------\n"

    private val underLine = "<u>                                                </u>\n"

    private val header = "${createNewString("Description", 25)}${createNewString("Qty", 7)}" +
            "${createNewString("Price", 8)}${createNewString("Amount", 10)}\n"

    fun isPrintSelected() = flow {
        emit(ApisResponse.Loading("Checking Printer"))
        val connection = BluetoothPrintersConnections.selectFirstPaired()
        if (connection != null && connection.isConnected) {
            connection.disconnect()
            emit(ApisResponse.Success("Success"))
        } else {
            emit(ApisResponse.Error(printNotConnectedToProceed, null))
        }
    }.flowOn(IO)




    fun doPrint(responseBody: PrintReceiptInfo) = flow {
        emit(ApisResponse.Loading("Please Wait Printing Receipt ${getEmojiByUnicode(0x1F5A8)}"))

        val data=try {
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
                            "[L]" + "${
                        createNewString(
                            "Total Amt Excel. Of GST",
                            40
                        )
                    }${createNewString(ListOfFoodItemToSearchAdaptor.setPrice(responseBody.amtExclGST).toString(), 10)}\n" +
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
                    }${createNewString(ListOfFoodItemToSearchAdaptor.setPrice(responseBody.amtInclGST).toString(), 10)}\n" +
                            "[C]$line" +
                            "[L]" + "${createNewString("Total", 40)}${
                        createNewString(
                            ListOfFoodItemToSearchAdaptor.setPrice(responseBody.amtInclGST).toString(),
                            10
                        )
                    }\n" +
                            "[C]$underLine"
                )

                printer.printFormattedText(stringBuilder.toString())
                printer.disconnectPrinter()
                return@let ApisResponse.Success("Receipt Printed ${getEmojiByUnicode(0x1F5A8)}")
            } ?: ApisResponse.Error("Please connect to Printer",null)
        } catch (e: Exception) {
            Log.i("PRINT_ANUJ", "doPrint: ${e.localizedMessage}")
            ApisResponse.Error(null,e)
        }
        emit(data)
    }.flowOn(IO)



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


    private fun doPrintTest(responseBody: PrintReceiptInfo) = flow {
        emit("Printing")
        try {
            val connection: BluetoothConnection? = BluetoothPrintersConnections.selectFirstPaired()
            connection?.let {
                val printer = EscPosPrinter(connection, 203, 80f, 32)
                val stringBuilder = StringBuilder()

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
                            "[L]" + "${
                        createNewString(
                            "Total Amt Excel. Of GST",
                            40
                        )
                    }${createNewString(responseBody.amtExclGST, 10)}\n" +
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
                    }${createNewString(responseBody.amtInclGST, 10)}\n" +
                            "[C]$line" +
                            "[L]" + "${createNewString("Total", 40)}${
                        createNewString(
                            responseBody.amtInclGST,
                            10
                        )
                    }\n" +
                            "[C]$underLine"
                )


                printer.printFormattedText(stringBuilder.toString())
                printer.disconnectPrinter()
                emit("Success")
            } ?: emit("Please connect to Printer")
        } catch (e: Exception) {
            emit("Error ${e.localizedMessage!!}")
        }
    }.flowOn(IO)



    private fun setTable(list: List<ItemList>): String {
        val stringBuilder = StringBuilder()
        list.forEach { item ->
            val value =
                "[L]${createNewString(item.description, 25)}${
                    createNewString(
                        "${item.qty}",
                        7
                    )
                }" +
                        createNewString(ListOfFoodItemToSearchAdaptor.setPrice(item.price).toString(), 8) +
                        "${createNewString(ListOfFoodItemToSearchAdaptor.setPrice(item.amount).toString(), 10)}\n"
            stringBuilder.append(value)
        }
        return stringBuilder.toString()
    }




}