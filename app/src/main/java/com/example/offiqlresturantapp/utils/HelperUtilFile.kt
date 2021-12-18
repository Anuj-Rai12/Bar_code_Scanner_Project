package com.example.offiqlresturantapp.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.ui.oderconfirm.model.FoodItemSelected
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItem
import com.example.offiqlresturantapp.ui.tablemange.model.TableData
import com.example.offiqlresturantapp.ui.tableorcost.model.SelectionDataClass
import kotlin.random.Random

fun AppCompatActivity.hide() {
    this.supportActionBar!!.hide()
}

fun AppCompatActivity.show() {
    this.supportActionBar!!.show()
}

fun View.hide() {
    this.isVisible = false
}

fun View.show() {
    this.isVisible = true
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

const val TAG = "ANUJ"
const val Rs_Symbol = "₹"
fun Context.msg(string: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, string, time).show()
}

fun rand(from: Int = 0, to: Int = 1): Int {
    return Random.nextInt(to - from) + from
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.changeStatusBarColor(color: Int = R.color.light_blue_bg) {
    this.window?.statusBarColor = resources.getColor(color, null)
}
typealias ItemClickListerForTableOrCost = (selection: SelectionDataClass) -> Unit

typealias ItemClickListerForTableData = (tableData: TableData) -> Unit
typealias ItemClickListerForListOfFood = (foodItem: FoodItem) -> Unit
typealias ItemClickListerForFoodSelected = (foodItemSelected: FoodItemSelected) -> Unit
