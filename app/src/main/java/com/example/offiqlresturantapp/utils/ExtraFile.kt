package com.example.offiqlresturantapp.utils

import android.util.Base64

object ApiPostResponseObj {
    const val _xmlns = "http://schemas.xmlsoap.org/soap/envelope/"
    const val _xmls = "urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI"
    private const val userName = "Test"
    private const val PASSWORD = "Test@123"
    const val base = "$userName:$PASSWORD"
    var token = genToken()
    var authHeader = "Basic $token"
}

fun genToken(): String = Base64.encodeToString(ApiPostResponseObj.base.toByteArray(), Base64.NO_WRAP)

