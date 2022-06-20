package com.example.offiqlresturantapp.data.cofirmDining.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = "s:Envelope", strict = false)
data class ConfirmDiningErrorResponse(

    @field:Element(name = "s:Fault", required = false)
    @param:Element(name = "s:Fault", required = false)
    @field:Path("s:Body")
    @param:Path("s:Body")
    val body: ConfirmDiningErrorBody? = null,

)


@Root(name = "s:Fault", strict = false)
data class ConfirmDiningErrorBody @JvmOverloads constructor(

    @field:Element(name = "string", required = false)
    @param:Element(name = "string", required = false)
    @field:Path("detail")
    @param:Path("detail")
    val errorMessage: String? = null

)
