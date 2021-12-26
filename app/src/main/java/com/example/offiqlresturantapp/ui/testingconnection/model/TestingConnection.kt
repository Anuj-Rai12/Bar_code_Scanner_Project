package com.example.offiqlresturantapp.ui.testingconnection.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestingBarcodeConnection(
    val title: String?,
    val uri:String?
) : Parcelable