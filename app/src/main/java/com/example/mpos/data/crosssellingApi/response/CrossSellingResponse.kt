package com.example.mpos.data.crosssellingApi.response

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
class CrossSellingResponse(
    @field:Element(name = "CrossSellingAPI_Result", required = false)
    @param:Element(name = "CrossSellingAPI_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: CrossSellingResponseBody? = null,
)

@Root(name = "CrossSellingAPI_Result", strict = false)
class CrossSellingResponseBody @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val returnValue: String? = null,
)
