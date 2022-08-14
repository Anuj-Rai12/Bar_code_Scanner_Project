package com.example.mpos.data.confirmOrder

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = "Envelope", strict = false)
data class ConfirmOrderRequest(

    @field:Element(name = "ConfirmOrder", required = false)
    @param:Element(name = "ConfirmOrder", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: ConfirmOrderBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns
)

@Root(name = "ConfirmOrder", strict = false)
data class ConfirmOrderBody(

    @field:Element(name = "receiptNo")
    @param:Element(name = "receiptNo")
    val receiptNo: String? = null,

    @field:Element(name = "getBillDetailFlag")
    @param:Element(name = "getBillDetailFlag")
    val getPrintBody: String = false.toString(),

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls,
)
