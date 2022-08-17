package com.example.mpos.data.deals.confirmdeals

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class ConfirmDealsRequest @JvmOverloads constructor(

    @field:Element(name = "ConfirmDealPressed", required = false)
    @param:Element(name = "ConfirmDealPressed", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: ConfirmDealsOrderBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns

)


@Root(name = "ConfirmDealPressed", strict = false)
data class ConfirmDealsOrderBody @JvmOverloads constructor(

    @field:Element(name = "rcptNo")
    @param:Element(name = "rcptNo")
    val rcptNo: String? = null,


    @field:Element(name = "dealCode")
    @param:Element(name = "dealCode")
    val dealsCode: String? = null,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)
