package com.example.offiqlresturantapp.model.test.apklogin

import com.example.offiqlresturantapp.utils.ApiPostResponseObj
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
data class EnvelopePostItem @JvmOverloads constructor(

    @field:Element(name = "APKlogin", required = false)
    @param:Element(name = "APKlogin", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val apk: APKLogin?=null,
    /*@field:Element(name = "Body", required = false)
    @param:Element(name = "Body", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: Body,*/

    /*@Attribute(name = "_xmlns")
    val xmlns: String = ApiPostResponseObj._xmlns*/
)

/*@Root(name = "Body", strict = false)
data class Body constructor(
    @field:Element(name = "APKlogin", required = false)
    @param:Element(name = "APKlogin", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val apk: APKLogin
)*/


@Root(name = "APKlogin", strict = false)
data class APKLogin @JvmOverloads constructor(
    @field:Element(name = "storeNo")
    @param:Element(name = "storeNo")
    val storeNo: String? = null,

    @field:Element(name = "userID")
    @param:Element(name = "userID")
    val userID: String? = null,

    @field:Element(name = "password")
    @param:Element(name = "password")
    val password: String? = null,

    /*@Attribute(name = "_xmlns")
    val xmlns: String = ApiPostResponseObj._xmls*/
)