package com.example.motionlyt.model.data

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class FileData(
    val fileName: String? = null,
    val fileUrl: String? = null,
    val date: String? = null,
    val type: String? = null,
    val size: String? = null
)