package com.example.mpos.data.feedback.invoice


import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class FinalInvoiceSendResponse @JvmOverloads constructor(

    @field:Element(name = "FinalInvoiceSend_Result", required = false)
    @param:Element(name = "FinalInvoiceSend_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: ResponseFinalInvoiceSend? = null

)

@Root(name = "FinalInvoiceSend_Result", strict = false)
data class ResponseFinalInvoiceSend @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null
)