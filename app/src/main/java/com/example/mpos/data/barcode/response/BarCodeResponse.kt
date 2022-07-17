package com.example.mpos.data.barcode.response

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class BarCodeResponse @JvmOverloads constructor(
    @field:Element(name = "ScanAndFindITEM_Result", required = false)
    @param:Element(name = "ScanAndFindITEM_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val barCodeBody: Response? = null
)

@Root(name = "ScanAndFindITEM_Result", strict = false)
data class Response @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)