package com.example.mpos.data.reservation.response

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class GetTableReservationResponse @JvmOverloads constructor(
    @field: Element(name = "GetReservationDetails_Result", required = false)
    @param:Element(name = "GetReservationDetails_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val responseForBody: GetReservationDetailsResultBody? = null
)

@Root(name = "GetReservationDetails_Result", strict = false)
data class GetReservationDetailsResultBody @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)


