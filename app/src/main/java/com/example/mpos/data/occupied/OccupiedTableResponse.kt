package com.example.mpos.data.occupied

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class OccupiedTableResponse @JvmOverloads constructor(
    @field:Element(name = "OccupiedTableDetail_Result", required = false)
    @param:Element(name = "OccupiedTableDetail_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val occupiedTableBody: Response? = null
)

@Root(name = "OccupiedTableDetail_Result", strict = false)
data class Response @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)