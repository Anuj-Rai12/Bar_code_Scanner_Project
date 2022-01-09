package com.example.offiqlresturantapp.ui.testingconnection.model.api

import com.example.offiqlresturantapp.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
data class ApKLoginPost @JvmOverloads constructor(
    @field:Element(name = "APKlogin", required = false)
    @param:Element(name = "APKlogin", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val apK: ApkBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns //"http://schemas.xmlsoap.org/soap/envelope/"
)

@Root(name = "APKlogin", strict = false)
data class ApkBody @JvmOverloads constructor(
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
    val xmlns: String = AllStringConst._xmls //"urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI"
)
