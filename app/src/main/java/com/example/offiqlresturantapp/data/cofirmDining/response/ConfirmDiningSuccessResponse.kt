package com.example.offiqlresturantapp.data.cofirmDining.response

import com.example.offiqlresturantapp.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class ConfirmDiningSuccessResponse @JvmOverloads constructor(

    @field:Element(name = "CustomerDining_Result", required = false)
    @param:Element(name = "CustomerDining_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: ConfirmDiningSuccessBody? = null,

    )

@Root(name = "CustomerDining_Result", strict = false)
data class ConfirmDiningSuccessBody @JvmOverloads constructor(

    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val returnValue: String? = null,

    @field:Element(name = "errorFound")
    @param:Element(name = "errorFound")
    val errorFound: String? = null,

 /*   @field:Element(name = "errorText")
    @param:Element(name = "errorText")
    val errorText: String? = "",*/

/*    @field:Element(name = "contactNo")
    @param:Element(name = "contactNo")
    val contactNo: String? = null,*/


)
