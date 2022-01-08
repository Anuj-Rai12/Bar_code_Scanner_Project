package com.example.offiqlresturantapp.model.test.apkJanLogin

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
data class EnvelopeOption @JvmOverloads constructor(
    @field:Element(name = "APKlogin", required = false)
    @param:Element(name = "APKlogin", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val apK: APKLoginCls? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlattr: String = "http://schemas.xmlsoap.org/soap/envelope/"
)

@Root(name = "APKlogin", strict = false)
data class APKLoginCls @JvmOverloads constructor(
    @field:Element(name = "storeNo")
    @param:Element(name = "storeNo")
    val storeNo: String? = null,

    @field:Element(name = "userID")
    @param:Element(name = "userID")
    val userID: String? = null,

    @field:Element(name = "password")
    @param:Element(name = "password")
    val password: String? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = "urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI"
)
