package com.example.mpos.utils.print
/*

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.example.mpos.data.confirmOrder.response.json.PrintReceiptInfo
import com.example.mpos.data.confirmOrder.response.json.ItemList
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.getEmojiByUnicode
import com.example.mpos.utils.msg
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.StringBuilder

class PrintBill(
    private val activity: Activity,
    private val error: (msg: String) -> Unit,
    private val success: () -> Unit
) {

    private val connection = BluetoothPrintersConnections.selectFirstPaired()

    fun isBluetoothDeviceFound() = connection != null && connection.isConnected

    val printNotConnectedToProceed =
        "Cannot find the Printer${getEmojiByUnicode(0x1F5A8)} to Print Bill\nAre you sure you want Proceed without it?\n"

    private val printNotConnected =
        "Please Enable Bluetooth or\nCheck POS print is Connected Or Not?"

    private val line = "------------------------------------------------\n"

    private val underLine = "<u>                                                </u>\n"

    private val header = "${createNewString("Description", 25)}${createNewString("Qty", 7)}" +
            "${createNewString("Price", 8)}${createNewString("Amount", 10)}\n"


    private val orderNo = "Order No.:"
    private val desc = "Description"
    private val qty = "Qty"
    private val price = "Price"
    private val amt = "Amount"
    private val totalWithOutGst = "Total Amt Excl. Of GST."
    private val noOfItem = "No. of Items"
    private val totalWithGst = "Total Amt Inc. Of GST."
    private val total = "Total"

    companion object {
        fun setCashAnalytics(e: Exception) {
            Firebase.crashlytics.recordException(e)
        }
    }

    fun doPrint(responseBody: PrintReceiptInfo) {
        try {
            if (connection != null) {
                val printer = EscPosPrinter(connection, 203, 72f, 32)
                val text =
                    "[C]<u>                                               </u>\n" +
                            "[C]" + "<b>${responseBody.headerTxt}<b>\n" +
                            "[C]" + "${responseBody.headerTxt2}\n\n" +
                            addDash(printer) +
                            "[L] $orderNo" + responseBody.orderId + "[R]" + responseBody.datetime + "\n" +
                            addDash(printer) +
                            "[L]" + "$desc        " + "[R]" + "$qty   " + "[R]" + "$price   " + "[R]" + "$amt\n" +
                            addDash(printer) +
                            addTableInfo(list = responseBody.itemList) +
                            addDash(printer) +
                            "[L]" + totalWithOutGst + "[R]" + "\t\t<b>${responseBody.amtExclGST}</b>" + "\n" +
                            addDash(printer) +
                            "[L]" + noOfItem + "[R]" + "\t\t<b>${responseBody.itemList.size}</b>" + "\n" +
                            addDash(printer) +
                            "[L]" + totalWithGst + "[R]" + "\t\t<b>${responseBody.amtInclGST}</b>" + "\n" +
                            addDash(printer) +
                            "[L]" + total + "[R]" + "\t\t<b>${responseBody.amtExclGST}</b>" + "\n" +
                            addDash(printer, " ")
                printer.printFormattedText(text)
                success.invoke()
            } else {
                error.invoke(printNotConnected)
            }
        } catch (e: Exception) {
            Log.e("APP", "Can't print", e)
            activity.msg("Cannot Print Bill ${getEmojiByUnicode(0x1F5A8)}")
            setCashAnalytics(e)
            error.invoke(e.localizedMessage ?: "Unknown Error")
        }
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
    }.flowOn(Dispatchers.IO)

    private fun addTableInfo(list: List<ItemList>): String {
        val stringBuilder = StringBuilder()
        list.forEach { foodItem ->
            stringBuilder.append(
                "[L]" + setString(
                    foodItem.description,
                    desc + "\t\t\t"
                ) + "   " + "[R]" + foodItem.qty + "   " + "[R]" + foodItem.price + "   " + "[R]" + foodItem.amount
                        + "\n"
            )
        }
        return stringBuilder.toString()
    }


    private fun setTable(list: List<ItemList>): String {
        val stringBuilder = StringBuilder()
        list.forEach { item ->
            val value =
                "[L]${createNewString(item.description, 25)}${
                    createNewString(
                        "${item.qty}.00",
                        7
                    )
                }" +
                        "${createNewString(item.price, 8)}${createNewString(item.amount, 10)}\n"
            stringBuilder.append(value)
        }
        return stringBuilder.toString()
    }


    private fun setString(mainString: String, other: String): String {
        return if (mainString.length > other.length) {
            mainString.substring(0, other.length) + ".."
        } else {
            mainString
        }
    }

    private fun addDash(escPosPrinter: EscPosPrinter, type: String = "-"): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("[C]")
        if (type != "-")
            stringBuilder.append("<u>")
        for (i in 0 until escPosPrinter.printerCharSizeWidthPx) {
            stringBuilder.append(type)
        }
        if (type != "-")
            stringBuilder.append("</u>")

        stringBuilder.append("\n")
        return stringBuilder.toString()
    }

}*/
