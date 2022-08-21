package com.example.mpos.utils.print

import android.app.Activity
import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.example.mpos.data.confirmOrder.response.json.PrintReceiptInfo
import com.example.mpos.data.confirmOrder.response.json.ItemList
import com.example.mpos.utils.Rs_Symbol
import com.example.mpos.utils.getEmojiByUnicode
import com.example.mpos.utils.msg
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder

class PrintBill(
    private val activity: Activity,
    private val error: (msg: String) -> Unit
) {

    private val connection = BluetoothPrintersConnections.selectFirstPaired()

    fun isBluetoothDeviceFound() = connection != null && connection.isConnected

    val printNotConnectedToProceed =
        "Cannot find the Printer${getEmojiByUnicode(0x1F5A8)} to Print Bill\nAre you sure you want Proceed without it?\n"

    private val printNotConnected = "Please Enable Bluetooth or\nCheck POS print is Connected Or Not?"

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
                    "[C]<u>                                                  </u>\n" +
                            "[C]" + "<b>${responseBody.headerTxt}<b>\n" +
                            "[C]" + "${responseBody.headerTxt2}\n\n" +
                            "[C------------------------------------------------------\n" +
                            "[L] $orderNo" + responseBody.orderId + "[R]" + responseBody.datetime + "\n" +
                            "[C]-----------------------------------------------------\n" +
                            "[L]" + desc + "[R]" + qty + "[R]" + price + "[R]" + "$amt\n" +
                            addTableInfo(list = responseBody.itemList) +
                            "[C]-----------------------------------------------------\n" +
                            "[L]" + totalWithOutGst + "[R]" + "<b>${Rs_Symbol + responseBody.amtExclGST}</b>" + "\n" +
                            "[C]-----------------------------------------------------\n" +
                            "[L]" + noOfItem + "[R]" + "<b>${responseBody.itemList.size}</b>" + "\n" +
                            "[C]------------------------------------------------------\n" +
                            "[L]" + totalWithGst + "[R]" + "<b>${Rs_Symbol + responseBody.amtInclGST}</b>" + "\n" +
                            "[C]------------------------------------------------------\n" +
                            "[L]" + total + "[R]" + "<b>${Rs_Symbol + responseBody.amtExclGST}</b>" + "\n" +
                            "[C]<u>                                         </u>\n"
                printer.printFormattedText(text)
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
                "[L]" + foodItem.description + "[R]" + foodItem.qty + "[R]" + foodItem.price + "[R]" + foodItem.amount
                        + "\n"
            )
        }
        return stringBuilder.toString()
    }

}