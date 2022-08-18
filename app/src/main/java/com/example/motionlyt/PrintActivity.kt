package com.example.motionlyt

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.motionlyt.databinding.PrintActivityBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

class PrintActivity : AppCompatActivity() {

    private lateinit var binding: PrintActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrintActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val list = getRequestPermission()
        if (list.isEmpty()) {
            toastMsg("ALL PERMISSION GRANTED")
        }

        Dexter.withContext(this).withPermissions(list).withListener(object : PermissionListener,
            MultiplePermissionsListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {}

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                toastMsg(" WE NEED THE PERMISSION PRINT BILL")
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {}

            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {}

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {}

        }).check()

        binding.printSampleBill.setOnClickListener {

        }
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
        val flag=(ContextCompat.checkSelfPermission(this, string) == PackageManager.PERMISSION_GRANTED)
        Log.i("PRINT_PERMISSION", "checkSelfPermissionTest: $string permission is granted $flag")
        return flag
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