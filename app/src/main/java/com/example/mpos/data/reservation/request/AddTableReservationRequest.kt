package com.example.mpos.data.reservation.request

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class AddTableReservationRequest @JvmOverloads constructor(

    @field:Element(name = "AddReservation", required = false)
    @param:Element(name = "AddReservation", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: AddReservationBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmlns
)

@Root(name = "AddReservation", strict = false)
data class AddReservationBody @JvmOverloads constructor(


    @field:Element(name = "customerPhone", required = false)
    @param:Element(name = "customerPhone", required = false)
    val phoneNumber: String,


    @field:Element(name = "customerName", required = false)
    @param:Element(name = "customerName", required = false)
    val customerName: String,

    @field:Element(name = "reservationDate", required = false)
    @param:Element(name = "reservationDate", required = false)
    val reservationDate: String,

    @field:Element(name = "reservationTime", required = false)
    @param:Element(name = "reservationTime", required = false)
    val reservationTime: String,

    @field:Element(name = "cover", required = false)
    @param:Element(name = "cover", required = false)
    val cover: String,


    @field:Element(name = "reservationRemarks", required = false)
    @param:Element(name = "reservationRemarks", required = false)
    val reservationRemarks: String,


    @field:Element(name = "staffID", required = false)
    @param:Element(name = "staffID", required = false)
    var staffID: String="",

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls

)
