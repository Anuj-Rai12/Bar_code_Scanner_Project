package com.example.motionlyt

import android.app.Activity
import android.widget.Toast

fun Activity.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}