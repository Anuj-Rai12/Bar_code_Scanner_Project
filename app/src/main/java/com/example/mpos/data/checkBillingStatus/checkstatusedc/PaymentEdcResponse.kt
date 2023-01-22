package com.example.mpos.data.checkBillingStatus.checkstatusedc

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class PaymentEdcResponse @JvmOverloads constructor(

    @field:Element(name = "CheckStatusBillingFromEDC_Result", required = false)
    @param:Element(name = "CheckStatusBillingFromEDC_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: PaymentEdcResponseBody? = null,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns

)

@Root(name = "CheckStatusBillingFromEDC_Result", strict = false)
data class PaymentEdcResponseBody @JvmOverloads constructor(

    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val returnValue: String? = null

)