package com.example.mpos.data.printkot

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class PrintKotResponse @JvmOverloads constructor(
    @field:Element(name = "PrintKOT_Result", required = false)
    @param:Element(name = "PrintKOT_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val responseForBody: PrintKotBillResponseBody? = null
)

@Root(name = "PrintKOT_Result", strict = false)
data class PrintKotBillResponseBody @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)
