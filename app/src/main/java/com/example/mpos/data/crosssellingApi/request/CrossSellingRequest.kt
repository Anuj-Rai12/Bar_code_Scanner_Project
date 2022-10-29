package com.example.mpos.data.crosssellingApi.request

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class CrossSellingRequest(
    @field:Element(name = "CrossSellingAPI", required = false)
    @param:Element(name = "CrossSellingAPI", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: CrossSellingRequestBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns
)

@Root(name = "CrossSellingAPI", strict = false)
class CrossSellingRequestBody @JvmOverloads constructor(
    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls,

    @field:Element(name = "itemNo")
    @param:Element(name = "itemNo")
    val itemNo: String,

)
