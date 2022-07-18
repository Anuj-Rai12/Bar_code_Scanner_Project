package com.example.mpos.data.printbIll


import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class PrintBillRequest @JvmOverloads constructor(

    @field:Element(name = "PrintPreBill", required = false)
    @param:Element(name = "PrintPreBill", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: PrintBillRequestBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmlns

)


@Root(name = "PrintPreBill", strict = false)
data class PrintBillRequestBody @JvmOverloads constructor(

    @field:Element(name = "mDocNo", required = false)
    @param:Element(name = "mDocNo", required = false)
    val mDocNo: String? = null,



    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls

)
