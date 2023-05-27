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
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.example.mpos.R
import com.example.mpos.adaptor.AlertDialogListAdapter
import com.example.mpos.data.cofirmDining.ConfirmDiningBody
import com.example.mpos.data.cofirmDining.ConfirmDiningRequest
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.data.reservation.request.AddReservationBody
import com.example.mpos.data.reservation.request.AddTableReservationRequest
import com.example.mpos.databinding.ConfirmOrderDialogLayoutBinding
import com.example.mpos.databinding.QtyIncrementLayoutBinding
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.ui.tableorcost.model.SelectionDataClass
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.vmadalin.easypermissions.EasyPermissions
import retrofit2.Retrofit
import java.net.URL
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

const val CAMERA_INT = 11

const val BLUE_TOOTH = 211
const val BLUE_ADIMS = 121
const val BLUE_SCAN = 113
const val BLUE_CONNECT = 231

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

        const val printBill = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:PrintPreBill"

        const val categoryMenu = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:CategoryMenu"

        const val addReservation =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:AddReservation"

        const val getReservation =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:GetReservationDetails"
        const val costEstimation =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:ConfirmEstimation"

        const val logoutStaff = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:LogoutStaff"

        const val getDeals = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:DealMenu"

        const val scanAndFindDeals =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:ScanAndFindDeal"

        const val confirmBilling =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:ConfirmBilling"

        const val sendBillingEtc =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:SendBillingToEDC"

        const val BillingFromEDC=
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:BillingFromEDC"

        const val checkStatusBillingEDC =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:CheckStatusBillingEDC"

        const val printBillInvoice =
            "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:PrintFinalInvoice"

        const val crossSelling = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:CrossSellingAPI"
        const val printKot = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:PrintKOT"

        const val checkBillFROMEDC = "urn:microsoft-dynamics-schemas/codeunit/MPOSWSAPI:CheckStatusBillingFromEDC"
    }


    enum class API {
        RESTAURANT
    }
}

enum class WhereToGoFromSearch {
    TABLEMANGMENT, COSTESTIMATE, SHOWROOMESTIMATE, RESTAURANTESTIMATE, BILLPAYMENT, SHOWROOMBILLING, RESTARURANTBILLING
}


enum class WhereToGoFromScan {
    TESTINGCONNECTION, TABLEMANGMENT, COSTESTIMATE, SHOWROOMESTIMATE, RESTAURANTESTIMATE, BILLPAYMENT, SHOWROOMBILLING, RESTARURANTBILLING
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run {
        navigate(direction)
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


fun genToken(value: String): String = Base64.encodeToString(value.toByteArray(), Base64.NO_WRAP)

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

fun checkFieldValue(string: String) = string.isEmpty() || string.isBlank() || string=="null"

fun Button.showButtonProgress(string: String, color: Int) {
    showProgress {
        buttonText = string
        progressColor = color
    }
}

fun Button.hideProgress(string: String) {
    hideProgress(string)
}


fun Activity.getColorInt(color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(color, null)
    } else {
        resources.getColor(color)
    }
}

fun Context.getColorInt(color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(color, null)
    } else {
        resources.getColor(color)
    }
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


fun Activity.changeStatusBarColor(color: Int = R.color.light_blue_bg_two) {
    this.window?.statusBarColor = getColorInt(color)
}

@RequiresApi(Build.VERSION_CODES.M)
fun View.changeViewColor(color: Int) {
    this.setBackgroundColor(
        this.context.resources.getColor(
            color, null
        )
    )
}

val listOfBg by lazy {
    arrayOf(
        R.drawable.food_item_one_selcetion_layout,
        R.drawable.food_item_two_selection_layout,
        R.drawable.food_item_three_selection_layout
    )
}

fun Activity.checkCameraPermission() =
    EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)


fun Activity.checkBlueToothPermission() =
    EasyPermissions.hasPermissions(this, Manifest.permission.BLUETOOTH)


fun Activity.checkBlueAdminPermission() =
    EasyPermissions.hasPermissions(this, Manifest.permission.BLUETOOTH_ADMIN)


fun Activity.checkBlueConnectPermission() =
    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && EasyPermissions.hasPermissions(
        this, Manifest.permission.BLUETOOTH_CONNECT
    ))


fun Activity.checkBlueScanPermission() =
    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && EasyPermissions.hasPermissions(
        this, Manifest.permission.BLUETOOTH_SCAN
    ))

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
        this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )

    material.setView(binding.root).setTitle(title).setPositiveButton("Done") { dialog, _ ->
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
                contactNo = "0000000000",
                screenType = RestaurantSingletonCls.getInstance().getScreenType()!!
            )
        )
        msg("Saved")
        listener.invoke(confirmDiningRequest, true)
        dialog.dismiss()
    }.setCancelable(false).setNegativeButton("Cancel") { dialog, _ ->
        cancel.invoke()
        dialog.dismiss()
    }.create().show()
}




fun Activity.getCustomerInfoDialog(
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
        this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )

    material.setView(binding.root).setTitle(title).setPositiveButton("Done") { dialog, _ ->
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
                contactNo = "0000000000",
                screenType = RestaurantSingletonCls.getInstance().getScreenType()!!
            )
        )
        msg("Saved")
        listener.invoke(confirmDiningRequest, true)
        dialog.dismiss()
    }.setCancelable(false).setNegativeButton("Cancel") { dialog, _ ->
        cancel.invoke()
        dialog.dismiss()
    }.create().show()
}












fun Activity.addReservation(
    title: String, fragmentManager: FragmentManager, cancel: () -> Unit, listener: AddReservation
) {
    val binding = ConfirmOrderDialogLayoutBinding.inflate(layoutInflater)

    var isTimePickerClick = false
    var isDatePickerClick = false

    val material = MaterialAlertDialogBuilder(
        this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )
    material.setView(binding.root).setCancelable(false).setTitle(title)
        .setPositiveButton("Done") { dialog, _ ->
            val cover = binding.coverNumEd.text.toString()
            val phone = binding.customerNumberEd.text.toString()
            val name = binding.customerNameEd.text.toString()
            val time = binding.reservationTimeEd.text.toString()
            val date = binding.reservationDateEd.text.toString()
            val remark = binding.reservationRemarkEd.text.toString()

            if (checkFieldValue(cover) || checkFieldValue(phone) || checkFieldValue(name) || checkFieldValue(
                    time
                ) || checkFieldValue(date)
            ) {
                listener.invoke(null)
                msg("Please Add Correct Information")
                return@setPositiveButton
            }

            if (!cover.isDigitsOnly()) {
                msg("Please Add Correct Cover Number")
                listener.invoke(null)
                return@setPositiveButton
            } else if (cover.isDigitsOnly() && cover.toInt() <= 0) {
                msg("Cover Cannot be 0")
                listener.invoke(null)
                return@setPositiveButton
            }

            if (!phone.isDigitsOnly() || !isValidPhone(phone)) {
                msg("Please  Add Correct PhoneNumber")
                listener.invoke(null)
                return@setPositiveButton
            }

            listener.invoke(
                AddTableReservationRequest(
                    AddReservationBody(
                        phoneNumber = phone,
                        customerName = name,
                        reservationDate = date,
                        reservationTime = time,
                        reservationRemarks = if (checkFieldValue(remark)) "" else remark,
                        cover = cover
                    )
                )
            )
            dialog.dismiss()
        }.setNegativeButton("Cancel") { dialog, id ->
            cancel.invoke()
            dialog.dismiss()
        }.create().show()
    binding.customerNumTv.text = "Customer PhoneNo"
    binding.customerNameTxt.text = "Customer Name"
    binding.reserveTime.show()
    binding.reserveRemark.show()
    binding.reservationRemarkLayout.show()
    binding.reserveTime.text = "Reservation Time ${getEmojiByUnicode(0x23F0)}"
    binding.reservationDateEd.setText(getDate())
    binding.reservationTimeEdLayout.show()
    binding.reserveDate.show()
    binding.reserveDate.text = "Reservation Date ${getEmojiByUnicode(0x1F4C5)}"
    binding.reservationDateEdLayout.show()


    binding.reservationTimeEd.setOnClickListener {
        if (!isTimePickerClick) {
            isTimePickerClick = true
            timePicker(fragmentManager = fragmentManager, timeListener = { time ->
                binding.reservationTimeEd.setText(time)
                isTimePickerClick = false
            }, cancel = {
                isTimePickerClick = false
            })
        } else {
            msg("Opening Time Picker ${getEmojiByUnicode(0x23F0)}")
        }
    }

    binding.reservationDateEd.setOnClickListener {
        if (!isDatePickerClick) {
            isDatePickerClick = true
            calenderPicker(fragmentManager, cancel = {
                isDatePickerClick = false
            }, dateListener = { date ->
                isDatePickerClick = false
                binding.reservationDateEd.setText(date)
            })
        } else {
            msg("Opening Date Picker ${getEmojiByUnicode(0x1F4C5)}")
        }
    }
}


fun Activity.timePicker(
    fragmentManager: FragmentManager, timeListener: (txt: String) -> Unit, cancel: () -> Unit
) {

    //val isSystem24Hour = is24HourFormat(this)
    //val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

    val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
        .setTitleText("Select Booking Time").setHour(10).setMinute(56).build()

    timePicker.addOnPositiveButtonClickListener {
        val time = if (timePicker.hour < 12) {
            "${timePicker.hour}:${timePicker.minute} AM"
        } else {
            "${timePicker.hour}:${timePicker.minute} PM"
        }
        timeListener.invoke(time)
        timePicker.dismiss()
    }

    timePicker.addOnDismissListener {
        cancel.invoke()
        it.dismiss()
    }
    timePicker.addOnCancelListener {
        cancel.invoke()
        it.dismiss()
    }

    timePicker.show(fragmentManager, "Picker")

}


fun Activity.calenderPicker(
    fragmentManager: FragmentManager, cancel: () -> Unit, dateListener: (txt: String) -> Unit
) {
    val constraint = CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())
    val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Booking Date")
        .setCalendarConstraints(constraint.build()).build()


    datePicker.addOnCancelListener {
        cancel.invoke()
        it.dismiss()
    }

    datePicker.addOnDismissListener {
        cancel.invoke()
        it.dismiss()
    }

    datePicker.addOnPositiveButtonClickListener { time ->
        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = time
        val format = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate: String = format.format(calendar.time)
        dateListener.invoke(formattedDate)
    }
    datePicker.show(fragmentManager, "DatePicker")
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
        requireActivity(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog_simple
    )

    val dialog = material.setTitle(title).setMessage(desc).setIcon(icon).setCancelable(isCancel)
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
    value: String = "",
    type: String = "Quantity",
    isDecimal: Boolean,
    cancel: (Boolean) -> Unit,
    res: (ItemMaster) -> Unit,
    instruction: (String) -> Unit,
    amount: (ItemMaster) -> Unit
) {
    val materialDialogs = MaterialAlertDialogBuilder(
        requireActivity(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )

    val binding = QtyIncrementLayoutBinding.inflate(layoutInflater)
    val dialog = materialDialogs.setView(binding.root).setCancelable(isCancelable).show()
    binding.qtyEdLayout.hint = "Please Enter $type"
    if (!checkFieldValue(value)) binding.qtyEd.setText(value)
    if (type != "Quantity" && type != "Amount") {
        binding.qtyEd.inputType = InputType.TYPE_CLASS_TEXT
    } else {
        if (isDecimal) {
            binding.qtyEd.inputType =
                InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
    }
    binding.btnDone.setOnClickListener {
        val txt = binding.qtyEd.text.toString()
        if (checkFieldValue(txt)) {
            activity?.msg("Please Enter $type")
            return@setOnClickListener
        }
        when (type) {
            "Quantity" -> {
                val qty = binding.qtyEd.text.toString()
                try {
                    if (isDecimal) {
                        qty.toDouble()
                        if (qty.toDouble() <= 0.0) {
                            activity?.msg("$type should not be Zero")
                            return@setOnClickListener
                        }
                    }
                } catch (e: Exception) {
                    activity?.msg("Please Enter the Correct $type")
                    return@setOnClickListener
                }


                if (!isDecimal && !qty.isDigitsOnly()) {
                    activity?.msg("Please Enter Correct $type")
                    return@setOnClickListener
                }

                if (!isDecimal && qty.toInt() <= 0) {
                    activity?.msg("$type should not be Zero")
                    return@setOnClickListener
                }
                itemMaster.foodQty = "%.2f".format(qty.toDouble()).toDouble()
                val amt =
                    (ListOfFoodItemToSearchAdaptor.setPrice(itemMaster.salePrice) * itemMaster.foodQty)
                itemMaster.foodAmt = "%.4f".format(amt).toDouble()

                res.invoke(itemMaster)
            }
            "Amount" -> {
                var amt = binding.qtyEd.text.toString().toDoubleOrNull()
                if (amt == null) {
                    activity?.msg("Please Enter the Correct $type")
                    return@setOnClickListener
                }
                if (amt <= 0.0) {
                    activity?.msg("$type should not be Zero")
                    return@setOnClickListener
                }

                amt = "%.4f".format(amt).toDouble()
                val itemQty = (amt / ListOfFoodItemToSearchAdaptor.setPrice(itemMaster.salePrice))
                itemMaster.foodQty = "%.2f".format(itemQty).toDouble()
                itemMaster.foodAmt = amt
                amount.invoke(itemMaster)
            }
            else -> {
                instruction.invoke(txt)
            }
        }
        dialog.dismiss()
    }

    binding.btnCancel.setOnClickListener {
        cancel.invoke(false)
        dialog.dismiss()
    }
}


fun Activity.getDialogInput(
    hint: String, isCancelable: Boolean, view: View?, success: (String) -> Unit, cancel: () -> Unit
) {
    val materialDialogs = MaterialAlertDialogBuilder(
        this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )

    val binding = QtyIncrementLayoutBinding.inflate(layoutInflater)
    val dialog = materialDialogs.setView(binding.root).setCancelable(isCancelable).show()
    binding.btnCancel.setOnClickListener {
        cancel.invoke()
        dialog.dismiss()
    }
    binding.qtyEd.hint = hint
    binding.qtyEd.inputType = InputType.TYPE_CLASS_NUMBER
    binding.btnDone.setOnClickListener {
        val txt = binding.qtyEd.text.toString()
        if (checkFieldValue(txt)) {
            view?.showSandbar(
                "Please enter the Correct Value",
                Snackbar.LENGTH_LONG,
                getColorInt(R.color.color_red)
            ) {
                return@showSandbar "OK"
            }
            return@setOnClickListener
        }
        success.invoke(txt)
        dialog.dismiss()
    }
}


fun Activity.showDialogBoxToGetUrl(scan: () -> Unit, done: (String) -> Unit) {
    val materialDialogs = MaterialAlertDialogBuilder(
        this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )

    val binding = QtyIncrementLayoutBinding.inflate(layoutInflater)
    val dialog = materialDialogs.setView(binding.root).setCancelable(false).show()

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


fun Activity.dialogOption(list: List<String>, fragment: Fragment) {
    val materialDialogs = MaterialAlertDialogBuilder(
        this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )
    val adaptor = AlertDialogListAdapter(this)
    adaptor.addItem(list)
    materialDialogs.setTitle("Options")
    materialDialogs.setAdapter(adaptor) { dialog, which ->
        if (list[which] == list.last()) {
            dialog.dismiss()
            showDialogForDeleteInfo("${getEmojiByUnicode(0x1F5D1)} Swipe to delete")
        } else if (list[which].contains("About User")) {
            dialog.dismiss()
            val userID = RestaurantSingletonCls.getInstance().getUserId()
            val storeID = RestaurantSingletonCls.getInstance().getStoreId()
            fragment.showDialogBox(
                title = "About User", desc = "Store ID :$storeID\nUser ID :$userID"
            ) {}
        }
    }
    materialDialogs.show()

}


fun Activity.showDialogForDeleteInfo(title: String) {
    val materialDialogs = MaterialAlertDialogBuilder(
        this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )

    val binding = QtyIncrementLayoutBinding.inflate(layoutInflater)
    materialDialogs.setView(binding.root).show()
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
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(format)
        current.format(formatter)
    } else {
        val sdFormat = SimpleDateFormat(format)
        val date = sdFormat.format(Date())
        Log.i("DATE_ANUJ", "getDate: $date")
        return date

    }

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


fun createLogStatement(tag: String, msg: String) {
    Log.i(tag, "createLogStatement: $msg")
}




fun Fragment.showKeyBoard(view: View) {
    if (isAdded && activity != null) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
        view.requestFocus()
    }
}

fun Activity.hideKeyBoard(view: View) {
    val imm = (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}


typealias ItemClickListerForTableOrCost = (selection: SelectionDataClass) -> Unit
typealias ItemClickListerForListOfFood = (foodItem: ItemMasterFoodItem) -> Unit
typealias LumaListener = (lum: Double) -> Unit
typealias CustomerDining = (customer: ConfirmDiningRequest?, flag: Boolean) -> Unit
typealias ImageListener = (imageInput: InputImage) -> Unit
typealias SnackBarListener = (msg: String?) -> String?
typealias AddReservation = (data: AddTableReservationRequest?) -> Unit
//typealias ItemClickListerForFoodSelected = (foodItemSelected: FoodItemSelected) -> Unit
