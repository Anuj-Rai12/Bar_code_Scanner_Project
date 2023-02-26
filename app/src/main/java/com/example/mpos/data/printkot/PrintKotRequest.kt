package com.example.mpos.data.printkot

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class PrintKotRequest @JvmOverloads constructor(

    @field:Element(name = "PrintKOT", required = false)
    @param:Element(name = "PrintKOT", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: PrintKotForEDCBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmlns

)


@Root(name = "PrintKOT", strict = false)
data class PrintKotForEDCBody @JvmOverloads constructor(

    @field:Element(name = "mPOSDOC", required = false)
    @param:Element(name = "mPOSDOC", required = false)
    val mDocNo: String? = null,



    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls

)
