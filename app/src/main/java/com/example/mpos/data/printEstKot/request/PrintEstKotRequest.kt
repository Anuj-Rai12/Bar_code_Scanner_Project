package com.example.mpos.data.printEstKot.request

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class PrintEstKotRequest constructor(

    @field:Element(name = "PrintEstKOT", required = false)
    @param:Element(name = "PrintEstKOT", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: PrintEstKotRequestBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmlns

)

@Root(name = "PrintEstKOT", strict = false)
data class PrintEstKotRequestBody @JvmOverloads constructor(
    @field:Element(name = "mPOSDOC", required = false)
    @param:Element(name = "mPOSDOC", required = false)
    val mDocNo: String? = null,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls

)
