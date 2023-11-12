package com.example.mpos.data.printEstKot.response

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class PrintEstKotResponse @JvmOverloads constructor(
    @field:Element(name = "PrintEstKOT_Result", required = false)
    @param:Element(name = "PrintEstKOT_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val responseForBody: PrintEstKotResponseBody? = null
)

@Root(name = "PrintEstKOT_Result", strict = false)
data class PrintEstKotResponseBody @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)
