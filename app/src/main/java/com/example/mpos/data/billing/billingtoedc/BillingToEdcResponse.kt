package com.example.mpos.data.billing.billingtoedc

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class BillingToEdcResponse @JvmOverloads constructor(

    @field:Element(name = "BillingFromEDC_Result", required = false)
    @param:Element(name = "BillingFromEDC_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: BillingToEdcResponseBody? = null,

)

@Root(name = "BillingFromEDC_Result", strict = false)
data class BillingToEdcResponseBody @JvmOverloads constructor(

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
