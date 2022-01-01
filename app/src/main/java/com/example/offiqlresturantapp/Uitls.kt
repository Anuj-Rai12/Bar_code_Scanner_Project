package com.example.offiqlresturantapp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import java.lang.Exception
import java.util.ArrayList

const val PERMISSION_REQUESTS = 1
const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
fun Activity.allPermissionsGranted(): Boolean {
    for (permission in this.getRequiredPermission()!!) {
        if (permission == Manifest.permission.WRITE_SETTINGS) continue
        if (this.isPermissionGranted(permission!!)) {
            Log.d("allPermissionGranted", "permission not granted $permission")
            return false
        }
    }
    return true
}


fun Activity.getRequiredPermission(): Array<String?>? {
    return try {
        val info = packageManager
            .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        val ps = info.requestedPermissions
        if (ps != null && ps.isNotEmpty()) {
            ps
        } else {
            arrayOfNulls(0)
        }
    } catch (e: Exception) {
        arrayOfNulls(0)
    }
}


fun Activity.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) != PackageManager.PERMISSION_GRANTED
}


fun Activity.getRuntimePermissions() {
    val allNeededPermissions: MutableList<String> = ArrayList()
    for (permission in getRequiredPermission()!!) {
        if (isPermissionGranted(permission!!)) {
            allNeededPermissions.add(permission)
        }
    }
    if (allNeededPermissions.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
        )
    }
}


@RequiresApi(Build.VERSION_CODES.M)
fun Activity.changeStatusBarColor(color: Int = R.color.white) {
    this.window?.statusBarColor = resources.getColor(color, null)
}


fun AppCompatActivity.hide() {
    this.supportActionBar!!.hide()
}
fun View.hide(){
    this.isVisible=false
}

fun View.show(){
    this.isVisible=true
}