package com.example.mpos.use_case

import android.util.Log
import com.example.mpos.data.poslineitem.request.MenuItem
import com.example.mpos.data.poslineitem.request.MunItemContainer
import com.example.mpos.data.poslineitem.request.PosLineItemApiRequest
import com.example.mpos.data.poslineitem.request.RequestBody
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.utils.AllStringConst
import com.example.mpos.utils.TAG
import com.example.mpos.utils.getDate
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*

class ConfirmOrderUseCase {


    fun calGrandTotal(item: List<ItemMasterFoodItem>?): Double {
        var grandTotal = 0.0
        item?.let {
            it.forEach { food ->
                grandTotal += food.foodAmt
            }
        }
        return grandTotal
    }


    fun getCurrentDate(time: String) = flow {
        while (true) {
            val date = getCurrentDateTime()
            val dateInString = date.toString(time)
            val dateTime = dateInString.split(':')
            val hrs = dateTime.first().toInt()
            Log.i(TAG, "getCurrentDate: $dateInString")
            val item = when {
                hrs == 12 -> {
                    "$hrs:${dateTime.last()}PM"
                }
                (hrs - 12) == 12 -> {
                    "00:${dateTime.last()}AM"
                }
                hrs > 12 -> {
                    "${hrs - 12}:${dateTime.last()}PM"
                }
                else -> {
                    "${hrs}:${dateTime.last()}AM"
                }
            }
            emit(item)
            kotlinx.coroutines.delay(60000)
        }
    }


    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }


    fun getPosLineRequest(
        receipt: String,
        item: List<ItemMasterFoodItem>,
        time: String,
        storeNo: String
    ): PosLineItemApiRequest {
        val list = mutableListOf<MenuItem>()
        item.forEach { itemMasterFoodItem ->
            val menuItem = MenuItem(
                itemNo = itemMasterFoodItem.itemMaster.itemCode,
                receiptNo = receipt,
                qty = itemMasterFoodItem.foodQty.toString(),
                saleType = AllStringConst.API.RESTAURANT.name,
                date = getDate("MM/dd/yy") ?: "10/20/22",
                time = time,
                storeNo = storeNo,
                freeText = itemMasterFoodItem.free_txt,
                price = itemMasterFoodItem.itemMaster.salePrice,
                dealLine = itemMasterFoodItem.isDeal.toString().uppercase(Locale.getDefault())
            )
            list.add(menuItem)
        }
        return PosLineItemApiRequest(RequestBody(MunItemContainer(list)))
    }


}

