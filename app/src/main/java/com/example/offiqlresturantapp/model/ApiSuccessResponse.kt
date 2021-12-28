package com.example.offiqlresturantapp.model


import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


data class APKlogin_Result(
    val return_value: String?
)

/*data class Base(
    val envelope: Envelope?
)*/

data class BodyPost(
    val APKlogin_Result: APKlogin_Result?
)


@Root(name = "Envelope", strict = false)
data class EnvelopeSuccess(
    @field:Element(name = "Soap:Body", required = true)
    val body: BodyPost?
)


/*
@Root(name = "Envelope", strict = false)
data class EnvelopeSuccess(
    //  @Json(name = "Body")
    @Element(name = "Body")
    val body: BodySuccess,

    // @Json(name = "_xmlns:Soap")
    @Element(name = "_xmlns:Soap")
    val xmlnsSoap: String,

    //  @Json(name = "__prefix")
    @Element(name = "__prefix")
    val prefix: String
)

data class BodySuccess(
//    @Json(name = "APKlogin_Result")
    @Element(name = "APKlogin_Result")
    val apKLoginResult: APKLoginResult,

    //  @Json(name = "__prefix")
    @Element(name = "__prefix")
    val prefix: String
)

data class APKLoginResult(
    // @Json(name = "return_value")
    @Element(name = "return_value")
    val returnValue: String,

    //  @Json(name = "_xmlns")
    @Element(name = "_xmlns")
    val xmlns: String
)*/
