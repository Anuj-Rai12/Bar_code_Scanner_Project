package com.example.motionlyt

import android.Manifest
import android.content.pm.PackageManager
import android.icu.text.NumberFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.example.motionlyt.databinding.PrintActivityBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*


class PrintActivity : AppCompatActivity() {

    private lateinit var binding: PrintActivityBinding

    private val local = Locale("en", "IN")

    @RequiresApi(Build.VERSION_CODES.N)
    private val numeric = NumberFormat.getCurrencyInstance(local)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrintActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val list = getRequestPermission()
        if (list.isEmpty()) {
            toastMsg("ALL PERMISSION GRANTED")
        } else {
            getPermission(list)
        }
        binding.printSampleBill.setOnClickListener {
            if (getRequestPermission().isNotEmpty()) {
                getPermission(getRequestPermission())
                return@setOnClickListener
            }
            doPrint()
        }
    }

    private fun getPermission(list: List<String>) {
        Dexter.withContext(this).withPermissions(list).withListener(object : PermissionListener,
            MultiplePermissionsListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {}

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                toastMsg(" WE NEED THE PERMISSION PRINT BILL")
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
            }

            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {}

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
            }

        }).check()
    }

    private fun getRequestPermission(): List<String> {
        val mutableList = mutableListOf<String>()
        if (!checkSelfPermissionTest(Manifest.permission.BLUETOOTH)) {
            mutableList.add(Manifest.permission.BLUETOOTH)
        }
        if (!checkSelfPermissionTest(Manifest.permission.BLUETOOTH_ADMIN)) {
            mutableList.add(Manifest.permission.BLUETOOTH_ADMIN)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !checkSelfPermissionTest(Manifest.permission.BLUETOOTH_CONNECT)) {
            mutableList.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !checkSelfPermissionTest(Manifest.permission.BLUETOOTH_SCAN)) {
            mutableList.add(Manifest.permission.BLUETOOTH_SCAN)
        }
        return mutableList.toList()
    }

    private fun checkSelfPermissionTest(string: String): Boolean {
        val flag =
            (ContextCompat.checkSelfPermission(this, string) == PackageManager.PERMISSION_GRANTED)
        Log.i("PRINT_PERMISSION", "checkSelfPermissionTest: $string permission is granted $flag")
        return flag
    }


    private fun doPrint() {
        try {
            val connection = BluetoothPrintersConnections.selectFirstPaired()
            if (connection != null) {
                val printer = EscPosPrinter(connection, 203, 72f, 32)
                val text =
                    "[C]----------------------------------\n" +
                            "[C]" + "<b>MITHAI SHOP/SHOWROOM<b>\n" +
                            "[C]" + "AMRITSAR\n\n" +
                            "[C]------------------------------------\n" +
                            "[L] Order No .: " + "G1" + "[R]" + "08/10/2022 11:39:40 PM" + "\n" +
                            "[C]------------------------------------------------------------------------\n" +
                            "[L]" + "Description" + "[R]" + "QTY" + "[R]" + "Price" + "[R]" + "Amount\n" +
                            "[L]" + "Samosa" + "[R]" + "1" + "[R]" + "1,000" + "[R]" + "1,000\n" +
                            "[L]" + "Pizza" + "[R]" + "2" + "[R]" + "1,500" + "[R]" + "2,000\n" +
                            "[L]" + "Noodle" + "[R]" + "3" + "[R]" + "500" + "[R]" + "1,500\n" +
                            "[L]" + "Burger" + "[R]" + "5" + "[R]" + "700" + "[R]" + "6,000\n" +
                            "[C]-----------------------------------------------------------------------\n" +
                            "[L]" + "Total Amt Excel. Of GST" + "[R]" + "<b>10,000</b>" + "\n" +
                            "[C]-------------------------------------------------------\n" +
                            "[L]" + "No. of Items " + "[R]" + "<b>3.00</br>" + "\n" +
                            "[C]--------------------------------------------------------\n" +
                            "[L]" + "Total Amt Inc. Of GST" + "[R]" + "<b>10,000</b>" + "\n" +
                            "[C]-------------------------------------------------------\n" +
                            "[L]" + "Total " + "[R]" + "<b>279.00</b>" + "\n" +
                            "[C]------------------------------------------------------\n"
                printer.printFormattedText(text)
            } else {
                showError("Please Enable Bluetooth or\n Check POS print is Connected Or NOT?")
            }
        } catch (e: Exception) {
            Log.e("APP", "Can't print", e)
            showError(e.localizedMessage ?: "Unknown Error")
        }
    }

    private fun showError(msg: String) {
        AlertDialog.Builder(this).setTitle("Failed")
            .setMessage(msg)
            .setPositiveButton("Ok") { _, _ -> }
            .show()
        /*DialogOnDeniedPermissionListener.Builder
            .withContext(this)
            .withTitle("Failed")
            .withMessage(msg)
            .withButtonText("Ok")
            .build()*/
    }

    /* private fun getPermission() {
         if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
         } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, MainActivity.PERMISSION_BLUETOOTH_ADMIN);
         } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, MainActivity.PERMISSION_BLUETOOTH_CONNECT);
         } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, MainActivity.PERMISSION_BLUETOOTH_SCAN);
         } else {
             // Your code HERE
         }*/
}