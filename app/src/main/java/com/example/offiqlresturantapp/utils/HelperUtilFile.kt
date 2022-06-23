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
import androidx.fragment.app.Fragment
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.data.cofirmDining.ConfirmDiningBody
import com.example.offiqlresturantapp.data.cofirmDining.ConfirmDiningRequest
import com.example.offiqlresturantapp.data.item_master_sync.json.ItemMaster
import com.example.offiqlresturantapp.databinding.ConfirmOrderDialogLayoutBinding
import com.example.offiqlresturantapp.databinding.QtyIncrementLayoutBinding
import com.example.offiqlresturantapp.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.offiqlresturantapp.ui.searchfood.model.ItemMasterFoodItem
import com.example.offiqlresturantapp.ui.tableorcost.model.SelectionDataClass
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.vmadalin.easypermissions.EasyPermissions
import retrofit2.Retrofit
import java.text.Normalizer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern
import kotlin.random.Random

const val CAMERA_INT = 11
const val Url_barcode = Barcode.TYPE_URL
const val Url_Text = Barcode.TYPE_TEXT
fun AppCompatActivity.hide() {
    this.supportActionBar!!.hide()
}

object AllStringConst {
    const val BASE_URL = "http://20.204.153.37:7051/User1/WS/CKLifeStyle/Codeunit/"


    const val BASE_URL_2 = "http://20.204.153.37:7051/Navuser/WS/CKLifeStyle/Codeunit/"

    //http://223.31.53.229:28360/NAVUSER/WS/HLDGRP/Codeunit/
    const val _xmlns = "http://schemas.xmlsoap.org/soap/envelope/"
    const val _xmls = "urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI"
    private const val userName = "Online"
    private const val PASSWORD = "Pass@1234"
    const val PREFERENCES_USER = "User_INFO"
    const val base = "$userName:$PASSWORD"
    const val End_point = "LoginAndGetMasterAPI"
    const val Envelope = "Envelope"

    //const val No_Error = "No Error"
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

fun makeStringAlphaNumericForm(name: String): String {
    //val name = "12434adfjks%^&$" //"I>télé"; //example
    val normalized: String = Normalizer.normalize(name, Normalizer.Form.NFD)
    return normalized.replace("[^A-Za-z0-9]".toRegex(), "")
}

fun randomNumber(value: Long): Long = kotlin.math.floor((Math.random() * value)).toLong()
fun isValidPhone(phone: String): Boolean {
    val phonetic = "^[+]?[0-9]{10,13}\$"
    val pattern = Pattern.compile(phonetic)
    return pattern.matcher(phone).matches()
}


fun Activity.addDialogMaterial(
    title: String,
    time: String,
    tableNo: String,
    receiptNo: Long,
    storeVar: String,
    staffID: String,
    listener: CustomerDining
) {
    val binding = ConfirmOrderDialogLayoutBinding.inflate(layoutInflater)


    val material = MaterialAlertDialogBuilder(
        this,
        R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )

    material
        .setView(binding.root)
        .setTitle(title)
        .setPositiveButton("Done") { dialog, _ ->
            val customerName = binding.customerNameEd.text.toString()
            val customerNumber = binding.customerNumberEd.text.toString()
            val terminalNumber = binding.terminalNumEd.text.toString()
            if (checkFieldValue(customerName) || checkFieldValue(customerNumber) || checkFieldValue(
                    terminalNumber
                )
                || !isValidPhone(customerNumber)
            ) {
                msg("Please Enter Current Info \n Try Again.")
                return@setPositiveButton
            }
            val confirmDiningRequest = ConfirmDiningRequest(
                body = ConfirmDiningBody(
                    rcptNo = "$receiptNo",
                    customerPhone = customerNumber,
                    customerName = customerName,
                    covers = binding.coverValue.text.toString(),
                    storeVar = storeVar,
                    tableNo = tableNo,
                    terminalNo = makeStringAlphaNumericForm(terminalNumber),
                    errorFound = false.toString(),
                    salesType = "RESTAURANT",
                    staffID = staffID,
                    transDate = getDate() ?: "2022-06-18",
                    transTime = time,
                    waiterName = "",
                    waiterID = "",
                    errorText = "",
                    contactNo = "0000000000"
                )
            )
            listener.invoke(confirmDiningRequest)
            dialog.dismiss()
        }.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .create().show()

    binding.incrementBtn.setOnClickListener {
        binding.coverValue.apply {
            val value = this.text.toString().toInt()
            this.text = (value + 1).toString()
        }
    }

    binding.decrementBtn.setOnClickListener {
        binding.coverValue.apply {
            val value = this.text.toString().toInt()
            if (value > 1) {
                this.text = (value - 1).toString()
            }
        }
    }
}


fun Fragment.showDialogBox(
    title: String,
    desc: String,
    btn: String = "Ok",
    icon: Int = R.drawable.ic_info,
    listener: () -> Unit
) {
    val material = MaterialAlertDialogBuilder(
        requireActivity(),
        R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog_simple
    )

    material.setTitle(title)
        .setMessage(desc)
        .setIcon(icon)
        .setPositiveButton(btn) { dialog, _ ->
            listener.invoke()
            dialog.dismiss()
        }
        .show()

}


fun Fragment.showQtyDialog(
    title: String,
    isCancelable: Boolean = false,
    itemMaster: ItemMaster,
    cancel: (Boolean) -> Unit,
    res: (ItemMaster) -> Unit
) {
    val materialDialogs = MaterialAlertDialogBuilder(
        requireActivity(),
        R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )

    val binding = QtyIncrementLayoutBinding.inflate(layoutInflater)
    materialDialogs.setView(binding.root)
        .setTitle(title)
        .setCancelable(isCancelable)
        .setIcon(R.drawable.ic_info)
        .setPositiveButton("Done") { dialog, _ ->
            val qty = binding.coverValue.text.toString().toInt()
            itemMaster.foodQty = qty
            itemMaster.foodAmt = ListOfFoodItemToSearchAdaptor.setPrice(itemMaster.salePrice) * qty
            res.invoke(itemMaster)
            dialog.dismiss()
        }.setNegativeButton("Cancel") { dialog, _ ->
            cancel.invoke(false)
            dialog.dismiss()
        }.show()

    binding.incrementBtn.setOnClickListener {
        binding.coverValue.apply {
            val value = this.text.toString().toInt()
            this.text = (value + 1).toString()
        }
    }

    binding.decrementBtn.setOnClickListener {
        binding.coverValue.apply {
            val value = this.text.toString().toInt()
            if (value > 1) {
                this.text = (value - 1).toString()
            }
        }
    }

}


fun getDate(): String? {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return current.format(formatter)
}


typealias ItemClickListerForTableOrCost = (selection: SelectionDataClass) -> Unit
typealias ItemClickListerForListOfFood = (foodItem: ItemMasterFoodItem) -> Unit
typealias LumaListener = (lum: Double) -> Unit
typealias CustomerDining = (customer: ConfirmDiningRequest) -> Unit
typealias ImageListener = (imageInput: InputImage) -> Unit
typealias SnackBarListener = (msg: String?) -> String?
//typealias ItemClickListerForFoodSelected = (foodItemSelected: FoodItemSelected) -> Unit
