package com.example.mpos.data.deals.scan_and_find_deals

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class ScanAndFindDealsRequest @JvmOverloads constructor(
    @field:Element(name = "ScanAndFindDeal", required = false)
    @param:Element(name = "ScanAndFindDeal", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: ScanAndFindDealsRequestBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns

)

@Root(name = "ScanAndFindDeal", strict = false)
data class ScanAndFindDealsRequestBody @JvmOverloads constructor(


    @field:Element(name = "dealInput")
    @param:Element(name = "dealInput")
    val dealsCode: String,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)
