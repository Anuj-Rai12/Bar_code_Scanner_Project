package com.example.mpos.data.billing.billingtoedc

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class BillingFromEDCRequest @JvmOverloads constructor(
    @field:Element(name = "BillingFromEDC", required = false)
    @param:Element(name = "BillingFromEDC", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: BillingToEdcRequestBody? = null,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns
)

@Root(name = "BillingFromEDC", strict = false)
data class BillingToEdcRequestBody @JvmOverloads constructor(

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls,

    @field:Element(name = "rcptNo")
    @param:Element(name = "rcptNo")
    val rcptNo: String,

    @field:Element(name = "transDate")
    @param:Element(name = "transDate")
    val transDate: String,

    @field:Element(name = "transTime")
    @param:Element(name = "transTime")
    val transTime: String,

    @field:Element(name = "salesType")
    @param:Element(name = "salesType")
    val salesType: String=AllStringConst.API.RESTAURANT.name,


    @field:Element(name = "storeVar")
    @param:Element(name = "storeVar")
    val storeVar: String,


    @field:Element(name = "errorFound")
    @param:Element(name = "errorFound")
    val errorFound: String=false.toString(),


    @field:Element(name = "errorText")
    @param:Element(name = "errorText")
    val errorText: String="",


    @field:Element(name = "contactNo")
    @param:Element(name = "contactNo")
    val contactNo: String="useless String",



    @field:Element(name = "terminalNo")
    @param:Element(name = "terminalNo")
    val terminalNo: String="",


    @field:Element(name = "staffID")
    @param:Element(name = "staffID")
    val staffID: String,

)
