package com.example.mpos.data.billing.printInvoice.response

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Soap_Envelope, strict = false)
class PrintInvoiceResponse @JvmOverloads constructor(
    @field:Element(name = "PrintFinalInvoice_Result", required = false)
    @param:Element(name = "PrintFinalInvoice_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: PrintInvoiceResponseBody? = null
)


@Root(name = "CheckStatusBillingEDC_Result", strict = false)
class PrintInvoiceResponseBody @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val returnValue: String? = null
)
