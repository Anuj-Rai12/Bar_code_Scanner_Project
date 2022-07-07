package com.fbts.mpos.data.cofirmDining

import android.os.Parcelable
import com.fbts.mpos.utils.AllStringConst
import kotlinx.parcelize.Parcelize
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Parcelize
@Root(name = AllStringConst.Envelope, strict = false)
data class ConfirmDiningRequest @JvmOverloads constructor(
    @field:Element(name = "CustomerDining", required = false)
    @param:Element(name = "CustomerDining", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: ConfirmDiningBody? = null,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns
) : Parcelable


@Parcelize
@Root(name = "CustomerDining", strict = false)
data class ConfirmDiningBody(
    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls,

    @field:Element(name = "rcptNo")
    @param:Element(name = "rcptNo")
    val rcptNo: String? = null,

    @field:Element(name = "custPhone")
    @param:Element(name = "custPhone")
    val customerPhone: String? = null,

    @field:Element(name = "custName")
    @param:Element(name = "custName")
    val customerName: String? = null,

    @field:Element(name = "covers")
    @param:Element(name = "covers")
    val covers: String? = null,

    @field:Element(name = "waiterName")
    @param:Element(name = "waiterName")
    val waiterName: String? = null,

    @field:Element(name = "tableNo")
    @param:Element(name = "tableNo")
    val tableNo: String? = null,

    @field:Element(name = "transDate")
    @param:Element(name = "transDate")
    val transDate: String? = null,

    @field:Element(name = "transTime")
    @param:Element(name = "transTime")
    val transTime: String? = null,

    @field:Element(name = "waiterId")
    @param:Element(name = "waiterId")
    val waiterID: String? = null,

    @field:Element(name = "salesType")
    @param:Element(name = "salesType")
    val salesType: String? = null,

    @field:Element(name = "storeVar")
    @param:Element(name = "storeVar")
    val storeVar: String? = null,

    @field:Element(name = "errorFound")
    @param:Element(name = "errorFound")
    val errorFound: String? = null,

    @field:Element(name = "errorText")
    @param:Element(name = "errorText")
    val errorText: String? = null,

    @field:Element(name = "contactNo")
    @param:Element(name = "contactNo")
    val contactNo: String? = null,

    @field:Element(name = "terminalNo")
    @param:Element(name = "terminalNo")
    val terminalNo: String? = null,

    @field:Element(name = "staffID")
    @param:Element(name = "staffID")
    val staffID: String? = null,//User Name Log In

) : Parcelable
