package com.example.mpos.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mpos.R
import com.example.mpos.data.cofirmDining.ConfirmDiningBody
import com.example.mpos.data.cofirmDining.ConfirmDiningRequest
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.ConfirmOrderDialogLayoutBinding
import com.example.mpos.databinding.QtyIncrementLayoutBinding
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.ui.tableorcost.model.SelectionDataClass
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.vmadalin.easypermissions.EasyPermissions
import retrofit2.Retrofit
import java.net.URL
import java.text.Normalizer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

const val CAMERA_INT = 11
const val Url_barcode = Barcode.TYPE_URL
const val Url_Text = Barcode.TYPE_TEXT
fun AppCompatActivity.hide() {
    this.supportActionBar!!.hide()
}

object AllStringConst {
    //  const val BASE_URL = "http://20.204.153.37:7051/Navuser/WS/CKLifeStyle/Codeunit/"

    //http://223.31.53.229:28360/NAVUSER/WS/HLDGRP/Codeunit/
    const val _xmlns = "http://schemas.xmlsoap.org/soap/envelope/"
    const val _xmls = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI"
    const val _xmlsList = "urn:microsoft-dynamics-nav/xmlports/x50006"

    // private const val userName = "Online"
    // private const val PASSWORD = "Pass@1234"
    const val PREFERENCES_USER = "User_INFO"

    //const val base = "$userName:$PASSWORD"
    const val End_point = "MPOSWSAPI"
    const val Envelope = "Envelope"

    //const val No_Error = "No Error"
    const val Soap_Envelope = "Soap:Envelope"
    //var authHeader = "Basic ${genToken(base)}"

    fun getAuthHeader(token: String) = "Basic $token"

    object SoapAction {
        const val HeaderKey = "SOAPAction: "
        const val ApkLogin = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:Login"
        const val confirmOrder = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:ConfirmOrder"
        const val confirmDining = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:CustomerDining"
        const val itemMasterSync =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:ItemMasterSync"
        const val tblInformation =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:TableInformation"

        const val Barcode = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:ScanAndFindITEM"

        const val PosLineItem = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:POSLineItems"
        const val occupiedTbl =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:OccupiedTableDetail"

        const val testConnection =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:ValidateStoreNo"
    }


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


fun genToken(value: String): String =
    Base64.encodeToString(value.toByteArray(), Base64.NO_WRAP)

fun <T> serializeToJson(bmp: T): String? {
    val gson = Gson()
    return gson.toJson(bmp)
}


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

/*fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}*/

const val TAG = "ANUJ"
const val Rs_Symbol = "₹"
fun Context.msg(string: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, string, time).show()
}

/*fun rand(from: Int = 0, to: Int = 2): Int {
    return Random.nextInt(to - from) + from
}*/

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.changeStatusBarColor(color: Int = R.color.light_blue_bg_two) {
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
    //val name = "12434adfjks%^&$" //"I>télé"; //fbts
    val normalized: String = Normalizer.normalize(name, Normalizer.Form.NFD)
    return normalized.replace("[^A-Za-z0-9]".toRegex(), "")
}


fun Fragment.removeItemFromBackStack() {
    val p = activity?.supportFragmentManager
    p?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun Fragment.showCountOfBackStack() {
    val fm = activity?.supportFragmentManager
    val count = fm?.backStackEntryCount
    Log.i(TAG, "showCountOfBackStack: Back Entry is ->$count")
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
    receiptNo: String,
    storeVar: String,
    staffID: String,
    cancel: () -> Unit,
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
            val customerName = binding.customerNameEd.text?.toString()
            val customerNumber = binding.customerNumberEd.text?.toString()
            val coverNumber = binding.coverNumEd.text.toString()
            if (checkFieldValue(coverNumber) || !coverNumber.isDigitsOnly()) {
                msg("Please Enter Correct Covers\n Try Again.")
                listener.invoke(null, false)
                return@setPositiveButton
            }
            if (!checkFieldValue(customerNumber.toString()) && !isValidPhone(customerNumber.toString())) {
                msg("Please Enter Correct Phone Number\n Try Again.")
                listener.invoke(null, false)
                return@setPositiveButton
            }
            if (coverNumber.isDigitsOnly() && coverNumber.toLong() <= 0) {
                msg("Convert cannot be Zero!!")
                listener.invoke(null, false)
                return@setPositiveButton
            }
            val confirmDiningRequest = ConfirmDiningRequest(
                body = ConfirmDiningBody(
                    rcptNo = receiptNo,
                    customerPhone = customerNumber ?: "",
                    customerName = customerName ?: "",
                    covers = coverNumber,
                    storeVar = storeVar,
                    tableNo = tableNo,
                    terminalNo = "",
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
            msg("Saved")
            listener.invoke(confirmDiningRequest, true)
            dialog.dismiss()
        }.setCancelable(false)
        .setNegativeButton("Cancel") { dialog, _ ->
            cancel.invoke()
            dialog.dismiss()
        }
        .create().show()
}


fun Fragment.showDialogBox(
    title: String,
    desc: String,
    btn: String = "Ok",
    icon: Int = R.drawable.ic_info,
    cancel: String? = null,
    isCancel: Boolean = true,
    listener: () -> Unit
) {
    val material = MaterialAlertDialogBuilder(
        requireActivity(),
        R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog_simple
    )

    val dialog = material.setTitle(title)
        .setMessage(desc)
        .setIcon(icon)
        .setCancelable(isCancel)
        .setPositiveButton(btn) { dialog, _ ->
            listener.invoke()
            dialog.dismiss()
        }
    cancel?.let {
        dialog.setNegativeButton(it) { dialog, _ ->
            dialog.dismiss()
        }
    }
    dialog.show()
}


fun Fragment.showQtyDialog(
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
    val dialog = materialDialogs.setView(binding.root)
        .setCancelable(isCancelable)
        .show()

    binding.btnDone.setOnClickListener {
        val qty = binding.qtyEd.text.toString()
        if (checkFieldValue(qty)) {
            activity?.msg("Please Enter Quantity")
            return@setOnClickListener
        }
        if (!qty.isDigitsOnly()) {
            activity?.msg("Please Enter Correct Quantity")
            return@setOnClickListener
        }
        itemMaster.foodQty = qty.toInt()
        itemMaster.foodAmt =
            ListOfFoodItemToSearchAdaptor.setPrice(itemMaster.salePrice) * qty.toInt()
        res.invoke(itemMaster)
        dialog.dismiss()
    }

    binding.btnCancel.setOnClickListener {
        cancel.invoke(false)
        dialog.dismiss()
    }
}


fun Activity.showDialogBoxToGetUrl(scan: () -> Unit, done: (String) -> Unit) {
    val materialDialogs = MaterialAlertDialogBuilder(
        this,
        R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )

    val binding = QtyIncrementLayoutBinding.inflate(layoutInflater)
    val dialog = materialDialogs.setView(binding.root)
        .setCancelable(false)
        .show()

    binding.qtyEdLayout.hint = "Please Add Url"
    binding.qtyEd.inputType = InputType.TYPE_CLASS_TEXT
    binding.btnCancel.text = "Scan"
    binding.btnDone.setOnClickListener {
        val url = binding.qtyEd.text.toString()
        if (checkFieldValue(url)) {
            msg("Please Enter the Url...")
            return@setOnClickListener
        }
        if (!isValidUrl(url)) {
            return@setOnClickListener
        }
        done.invoke(url.trim())
        dialog.dismiss()
    }

    binding.btnCancel.setOnClickListener {
        scan.invoke()
        dialog.dismiss()
    }
}


fun Activity.showDialogForDeleteInfo(title: String) {
    val materialDialogs = MaterialAlertDialogBuilder(
        this,
        R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )

    val binding = QtyIncrementLayoutBinding.inflate(layoutInflater)
    materialDialogs
        .setView(binding.root)
        .show()
    binding.qtyEd.hide()
    binding.btnDone.hide()
    binding.btnCancel.hide()
    binding.imageViewDelete.show()
    binding.textSizeForTitle.show()
    binding.lottieSwipe.show()
    binding.textSizeForTitle.text = title
}

fun getEmojiByUnicode(unicode: Int) = String(Character.toChars(unicode))

fun getDate(format: String = "yyyy-MM-dd"): String? {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern(format)
    return current.format(formatter)
}

fun Activity.isValidUrl(url: String?): Boolean {
    return try {
        URL(url).toURI()
        true
    } catch (e: java.lang.Exception) {
        msg("Please Enter valid Url")
        false
    }
}


typealias ItemClickListerForTableOrCost = (selection: SelectionDataClass) -> Unit
typealias ItemClickListerForListOfFood = (foodItem: ItemMasterFoodItem) -> Unit
typealias LumaListener = (lum: Double) -> Unit
typealias CustomerDining = (customer: ConfirmDiningRequest?, flag: Boolean) -> Unit
typealias ImageListener = (imageInput: InputImage) -> Unit
typealias SnackBarListener = (msg: String?) -> String?
//typealias ItemClickListerForFoodSelected = (foodItemSelected: FoodItemSelected) -> Unit
