package com.example.mpos.data.checkBillingStatus

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class CheckBillingStatusResponse(
    @field:Element(name = "CheckStatusBillingEDC_Result", required = false)
    @param:Element(name = "CheckStatusBillingEDC_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: CheckBillingStatusResponseBody? = null,
)

@Root(name = "CheckStatusBillingEDC_Result", strict = false)
data class CheckBillingStatusResponseBody @JvmOverloads constructor(

    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val returnValue: String? = null,

)



