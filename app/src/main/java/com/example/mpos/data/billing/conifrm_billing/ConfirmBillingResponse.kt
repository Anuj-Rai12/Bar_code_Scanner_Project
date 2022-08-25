package com.example.mpos.data.billing.conifrm_billing

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

data class ConfirmBillingResponse @JvmOverloads constructor(

    @field:Element(name = "ConfirmBilling_Result", required = false)
    @param:Element(name = "ConfirmBilling_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: ConfirmBillingResponseBody? = null,
)

@Root(name = "ConfirmBilling_Result", strict = false)
data class ConfirmBillingResponseBody @JvmOverloads constructor(

    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val returnValue: String? = null,

    @field:Element(name = "errorFound")
    @param:Element(name = "errorFound")
    val errorFound: String? = null,

    @field:Element(name = "errorText")
    @param:Element(name = "errorText")
    val errorText: String? = null,

    @field:Element(name = "contactNo")
    @param:Element(name = "contactNo")
    val contactNo: String? = null,
)

