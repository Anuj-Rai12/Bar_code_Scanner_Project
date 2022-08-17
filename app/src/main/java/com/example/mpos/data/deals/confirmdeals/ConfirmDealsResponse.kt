package com.example.mpos.data.deals.confirmdeals

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class ConfirmDealsResponse @JvmOverloads constructor(

    @field:Element(name = "ConfirmDealPressed_Result", required = false)
    @param:Element(name = "ConfirmDealPressed_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: ConfirmDealsResponseBody? = null,

)


@Root(name = "ConfirmDealPressed_Result", strict = false)
data class ConfirmDealsResponseBody @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null
)