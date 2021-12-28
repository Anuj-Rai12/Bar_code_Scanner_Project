package com.example.offiqlresturantapp.model

import com.example.offiqlresturantapp.utils.ApiPostResponseObj
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


data class APKlogin(
    //val xmlns: String?,
    val storeNo: String?,
    val password: String?,
    val userID: String?
)

/*data class Base(
    val Envelope: Envelope?
)*/

data class Body(
    val APKlogin: APKlogin?
)

data class Envelope(
    //val xmlns: String?,
    val Body: Body?
)


/*
@Root(name = "Envelope", strict = false)
data class EnvelopePost(
    @field:Element(name = "Body", required = true)
    val body: BodyPost,

    @field:Element(name = "_xmlns", required = true)
    val xmlns: String = ApiPostResponseObj._xmlns
)

data class BodyPost(
    //@Json(name = "APKlogin")
    @field:Element(name = "APKlogin", required = true)
    val apKLogin: APKLogin
)

data class APKLogin(
    val storeNo: String,
    val userID: String,
    val password: String,

    // @Json(name = "_xmlns")
    @Element(name = "_xmlns")
    val xmlns: String = ApiPostResponseObj._xmls
)*/
