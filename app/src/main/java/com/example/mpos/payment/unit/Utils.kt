package com.example.mpos.payment.unit

import android.util.Log

object Utils {

    fun createLogcat(tag:String,msg:String){
        Log.i(tag, "Result --> $msg")
    }

}