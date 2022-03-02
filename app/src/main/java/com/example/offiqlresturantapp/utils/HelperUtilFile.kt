package com.example.offiqlresturantapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.data.item_master_sync.json.ItemMaster
import com.example.offiqlresturantapp.ui.tableorcost.model.SelectionDataClass
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.vmadalin.easypermissions.EasyPermissions
import retrofit2.Retrofit
import kotlin.random.Random

const val CAMERA_INT = 11
const val Url_barcode = Barcode.TYPE_URL
fun AppCompatActivity.hide() {
    this.supportActionBar!!.hide()
}

object AllStringConst {
    const val BASE_URL = "http://20.204.153.37:7051/User1/WS/CKLifeStyle/Codeunit/"

    //http://223.31.53.229:28360/NAVUSER/WS/HLDGRP/Codeunit/
    const val _xmlns = "http://schemas.xmlsoap.org/soap/envelope/"
    const val _xmls = "urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI"
    private const val userName = "Test"
    private const val PASSWORD = "Test@123"
    const val PREFERENCES_USER = "User_INFO"
    const val base = "$userName:$PASSWORD"
    const val End_point = "LoginAndGetMasterAPI"
    const val Envelope = "Envelope"
    const val Soap_Envelope = "Soap:Envelope"
    var authHeader = "Basic ${genToken()}"

    enum class API {
        RESTAURANT
    }
}

fun View.showSandbar(
    msg: String,
    length: Int = Snackbar.LENGTH_SHORT,
    color: Int? = null,
    snackBarList: SnackBarListener? = null
) {
    val snackBar = Snackbar.make(this, msg, length)

    snackBarList?.let {
        snackBar.setAction(it(null)) {
            it(null)
            snackBar.dismiss()
        }
        snackBar.setActionTextColor(Color.WHITE)
        snackBar.setTextColor(Color.WHITE)
    }
    color?.let {
        snackBar.view.setBackgroundColor(it)
    }
    snackBar.show()
}


private fun genToken(): String =
    Base64.encodeToString(AllStringConst.base.toByteArray(), Base64.NO_WRAP)

fun <T> serializeToJson(bmp: T): String? {
    val gson = Gson()
    return gson.toJson(bmp)
}

/*data class OrderCollection(
    val foodItem: List<FoodItem>,
    val tableId:Int,
    val totalPeople:Int,
    val bookingTime:String,
    val grandTotal:Int,
)*/

inline fun <reified T> deserializeFromJson(jsonFile: String?): T? {
    val gson = Gson()
    return gson.fromJson(jsonFile, T::class.java)
}

inline fun <reified T> buildApi(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}

fun AppCompatActivity.show() {
    this.supportActionBar!!.show()
}

fun checkFieldValue(string: String) = string.isEmpty() || string.isBlank()

fun Button.showButtonProgress(string: String, color: Int) {
    showProgress {
        buttonText = string
        progressColor = color
    }
}

fun Button.hideProgress(string: String) {
    hideProgress(string)
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.getColorInt(color: Int): Int {
    return resources.getColor(color, null)
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

fun rand(from: Int = 0, to: Int = 2): Int {
    return Random.nextInt(to - from) + from
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.changeStatusBarColor(color: Int = R.color.light_blue_bg) {
    this.window?.statusBarColor = resources.getColor(color, null)
}

@RequiresApi(Build.VERSION_CODES.M)
fun View.changeViewColor(color: Int) {
    this.setBackgroundColor(
        this.context.resources.getColor(
            color,
            null
        )
    )
}

val listOfBg by lazy {
    arrayOf(
        R.drawable.food_item_one_selcetion_layout,
        R.drawable.food_item_two_selection_layout
    )
}

fun Activity.checkCameraPermission() =
    EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)


typealias ItemClickListerForTableOrCost = (selection: SelectionDataClass) -> Unit
typealias ItemClickListerForListOfFood = (foodItem: ItemMaster) -> Unit
typealias LumaListener = (lum: Double) -> Unit
typealias ImageListener = (imageInput: InputImage) -> Unit
typealias SnackBarListener = (msg: String?) -> String?
//typealias ItemClickListerForFoodSelected = (foodItemSelected: FoodItemSelected) -> Unit
