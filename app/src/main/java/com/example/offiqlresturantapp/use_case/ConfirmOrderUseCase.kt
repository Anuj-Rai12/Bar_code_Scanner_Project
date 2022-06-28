package com.example.offiqlresturantapp.use_case

import android.util.Log
import com.example.offiqlresturantapp.ui.searchfood.model.ItemMasterFoodItem
import com.example.offiqlresturantapp.utils.TAG
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*

class ConfirmOrderUseCase {


    fun calGrandTotal(item: List<ItemMasterFoodItem>?): Int {
        var grandTotal = 0
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
                    "\n$hrs:${dateTime.last()}PM"
                }
                (hrs - 12) == 12 -> {
                    "\n00:${dateTime.last()}AM"
                }
                hrs > 12 -> {
                    "\n${hrs-12}:${dateTime.last()}PM"
                }
                else -> {
                    "\n${hrs}:${dateTime.last()}AM"
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

}

