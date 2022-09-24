package com.example.mpos.data.billing.printInvoice.request

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class PrintInvoiceRequest(

    @field:Element(name = "PrintFinalInvoice", required = false)
    @param:Element(name = "PrintFinalInvoice", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: PrintInvoiceRequestBody? = null,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns
)

@Root(name = "PrintFinalInvoice", strict = false)
data class PrintInvoiceRequestBody(
    @field:Element(name = "mPOSDOC")
    @param:Element(name = "mPOSDOC")
    val mPosDoc: String,
    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls,
)
