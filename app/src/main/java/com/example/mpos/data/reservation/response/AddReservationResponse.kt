package com.example.mpos.data.reservation.response

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class AddReservationResponse(
    @field: Element(name = "AddReservation_Result", required = false)
    @param:Element(name = "AddReservation_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val responseForBody: AddReservationResponseResult? = null
)
@Root(name = "AddReservation_Result", strict = false)
data class AddReservationResponseResult @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)
