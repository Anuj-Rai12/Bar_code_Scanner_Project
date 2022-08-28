package com.example.mpos.utils.print

import android.app.Activity
import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.example.mpos.data.confirmOrder.response.json.PrintReceiptInfo
import com.example.mpos.data.confirmOrder.response.json.ItemList
import com.example.mpos.utils.getEmojiByUnicode
import com.example.mpos.utils.msg
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
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
                            "[L]" + "$desc        " + "[R]" + "$qty   "  + "[R]" + "$price   " + "[R]" + "$amt\n" +
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

}