package com.example.mpos.data.checkBillingStatus.checkstatusedc

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class PaymentEdcRequest @JvmOverloads constructor(

    @field:Element(name = "CheckStatusBillingFromEDC", required = false)
    @param:Element(name = "CheckStatusBillingFromEDC", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: PaymentEdcRequestBody? = null,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns

)

@Root(name = "CheckStatusBillingFromEDC", strict = false)
data class PaymentEdcRequestBody(
    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls,

    @field:Element(name = "mPOSDOC")
    @param:Element(name = "mPOSDOC")
    val mPosDoc: String,

    @field:Element(name = "eDCMachineStatus")
    @param:Element(name = "eDCMachineStatus")
    val edcMachineStatus: Boolean,

    @field:Element(name = "eDCResponse")
    @param:Element(name = "eDCResponse")
    val eDCResponse: String,

    @field:Element(name = "paymentType")
    @param:Element(name = "paymentType")
    val paymentType: String

)