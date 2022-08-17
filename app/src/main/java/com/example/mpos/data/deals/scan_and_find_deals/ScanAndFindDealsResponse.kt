package com.example.mpos.data.deals.scan_and_find_deals

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class ScanAndFindDealsResponse @JvmOverloads constructor(

    @field:Element(name = "ScanAndFindDeal_Result", required = false)
    @param:Element(name = "ScanAndFindDeal_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: ScanAndFindDealsResponseBody? = null,

)


@Root(name = "ScanAndFindDeal_Result", strict = false)
data class ScanAndFindDealsResponseBody @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null
)
