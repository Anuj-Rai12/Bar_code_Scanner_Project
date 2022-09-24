package com.example.mpos.data.checkBillingStatus

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class CheckBillingStatusRequest @JvmOverloads constructor(

    @field:Element(name = "CheckStatusBillingEDC", required = false)
    @param:Element(name = "CheckStatusBillingEDC", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: CheckBillingStatusRequestBody? = null,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns
)


@Root(name = "CheckStatusBillingEDC", strict = false)
data class CheckBillingStatusRequestBody(
    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls,

    @field:Element(name = "mPOSDOC")
    @param:Element(name = "mPOSDOC")
    val mPosDoc: String,

)
