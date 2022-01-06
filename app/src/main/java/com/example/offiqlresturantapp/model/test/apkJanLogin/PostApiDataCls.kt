package com.example.offiqlresturantapp.model.test.apkJanLogin

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "envelope", strict = false)
data class EnvelopeOption @JvmOverloads constructor(
    @field:Element(name = "APKlogin", required = false)
    @param:Element(name = "APKlogin", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val apK: APKLoginCls?=null
)

@Root(name = "APKlogin", strict = false)
data class APKLoginCls @JvmOverloads constructor(
    @field:Element(name = "storeNo")
    @param:Element(name = "storeNo")
    val storeNo: String?=null,

    @field:Element(name = "userID")
    @param:Element(name = "userID")
    val userID: String?=null,

    @field:Element(name = "password")
    @param:Element(name = "password")
    val password: String?=null,

    // @Json(name = "_xmlns")
    // val xmlns: String
)
