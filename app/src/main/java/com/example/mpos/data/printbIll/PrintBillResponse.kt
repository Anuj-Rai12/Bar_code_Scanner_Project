package com.example.mpos.data.printbIll


import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class PrintBillResponse @JvmOverloads constructor(
    @field:Element(name = "PrintPreBill_Result", required = false)
    @param:Element(name = "PrintPreBill_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val responseForBody: PrintBillResponseBody? = null
)

@Root(name = "PrintPreBill_Result", strict = false)
data class PrintBillResponseBody @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)
