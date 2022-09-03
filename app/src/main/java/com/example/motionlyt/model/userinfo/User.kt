package com.example.motionlyt.model.userinfo


import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val name: String? = null,
    val reg: String? = null,
    val uni: String? = null,
    val coursename: String?=null,
    val password: String? = null,
    val joindate: String? = null
)