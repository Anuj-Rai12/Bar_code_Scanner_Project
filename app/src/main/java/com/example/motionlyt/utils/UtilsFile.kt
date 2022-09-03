package com.example.motionlyt.utils

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.motionlyt.R
import com.google.android.material.snackbar.Snackbar

fun Activity.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.changeStatusBarColor(color: Int = R.color.white) {
    this.window?.statusBarColor = getColorInt(color)
}
fun Activity.getColorInt(color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(color, null)
    } else {
        resources.getColor(color)
    }
}

fun AppCompatActivity.hide() {
    this.supportActionBar!!.hide()
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

fun setLogCat(title: String = "TAG", msg: String) {
    Log.i(title, "$title: $msg")
}


fun View.showSnackBarMsg(msg: String, length: Int = Snackbar.LENGTH_SHORT, anchor: View?=null) {
    Snackbar.make(this, msg, length)
        .setAnchorView(anchor)
        .show()
}
