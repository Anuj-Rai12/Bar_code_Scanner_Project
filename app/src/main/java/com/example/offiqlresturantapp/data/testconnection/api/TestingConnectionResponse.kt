package com.example.offiqlresturantapp.data.testconnection.api

import com.example.offiqlresturantapp.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
class TestingConnectionResponse(

    @field:Element(name = "ValidateStoreNo_Result", required = false)
    @param:Element(name = "ValidateStoreNo_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: TestingConnectionResponseBody? = null

)


@Root(name = "ValidateStoreNo_Result", strict = false)
data class TestingConnectionResponseBody @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)



