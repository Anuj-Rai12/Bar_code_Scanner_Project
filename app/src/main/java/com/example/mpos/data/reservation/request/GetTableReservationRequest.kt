package com.example.mpos.data.reservation.request

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class GetTableReservationRequest @JvmOverloads constructor(

    @field:Element(name = "GetReservationDetails", required = false)
    @param:Element(name = "GetReservationDetails", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: GetTableReservationRequestBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmlns
)


@Root(name = AllStringConst.Envelope, strict = false)
data class GetTableReservationRequestBody @JvmOverloads constructor(

    @field:Element(name = "searchByMobile", required = false)
    @param:Element(name = "searchByMobile", required = false)
    val searchByMobile: String,


    @field:Element(name = "searchByName", required = false)
    @param:Element(name = "searchByName", required = false)
    val searchByName: String,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)
