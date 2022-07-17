package com.example.mpos.data.login.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestingBarcodeConnection(
    val title: String?,
    val uri:String?
) : Parcelable