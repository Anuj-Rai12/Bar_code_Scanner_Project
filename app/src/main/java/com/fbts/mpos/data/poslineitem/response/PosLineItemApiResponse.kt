package com.fbts.mpos.data.poslineitem.response

import com.fbts.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class PosLineItemApiResponse @JvmOverloads constructor(
    @field:Element(name = "POSLineItems_Result", required = false)
    @param:Element(name = "POSLineItems_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val responseForBody: Response? = null
)


@Root(name = "POSLineItems_Result", strict = false)
data class Response @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)