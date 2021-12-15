package com.example.offiqlresturantapp.utils

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.offiqlresturantapp.R

fun AppCompatActivity.hide() {
    this.supportActionBar!!.hide()
}

fun AppCompatActivity.show() {
    this.supportActionBar!!.show()
}
@RequiresApi(Build.VERSION_CODES.M)
fun Activity.changeStatusBarColor(color: Int = R.color.light_blue_bg) {
    this.window?.statusBarColor = resources.getColor(color, null)
}
