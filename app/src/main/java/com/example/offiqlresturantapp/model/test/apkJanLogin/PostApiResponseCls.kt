package com.example.offiqlresturantapp.model.test.apkJanLogin

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "Soap:Envelope", strict = false)
data class EnvelopePostApiResponse @JvmOverloads constructor(
    @field:Element(name = "APKlogin_Result", required = false)
    @param:Element(name = "APKlogin_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val apkLoginResult: APKLoginResult? = null

    /*@Json(name = "_xmlns:Soap")
    val xmlnsSoap: String,

    @Json(name = "__prefix")
    val prefix: String*/
)

/*data class Body(
    @Json(name = "APKlogin_Result")
    val apKloginResult: APKLoginResult,

    @Json(name = "__prefix")
    val prefix: String
)*/

@Root(name = "APKlogin_Result", strict = false)
data class APKLoginResult @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,

    /*@Json(name = "_xmlns")
    val xmlns: String*/
)
