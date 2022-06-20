package com.example.offiqlresturantapp.data.confirmOrder.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = "Soap:Envelope", strict = false)
data class ConfirmOrderSuccessResponse(

    @field:Element(name = "ConfirmOrder_Result", required = false)
    @param:Element(name = "ConfirmOrder_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: ConfirmOrderSuccessBody? = null,

)


@Root(name = "ConfirmOrder_Result", strict = false)
data class ConfirmOrderSuccessBody(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val returnValue: String? = null,

)
