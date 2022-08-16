package com.example.mpos.data.deals


import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class DealsRequest @JvmOverloads constructor(

    @field:Element(name = "DealMenu", required = false)
    @param:Element(name = "DealMenu", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: DealRequestBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns
)

@Root(name = "DealMenu", strict = false)
data class DealRequestBody @JvmOverloads constructor(

    @field:Element(name = "storeID")
    @param:Element(name = "storeID")
    val storeNo: String? = null,

    @field:Element(name = "salesType")
    @param:Element(name = "salesType")
    val type: String = AllStringConst.API.RESTAURANT.name,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)


