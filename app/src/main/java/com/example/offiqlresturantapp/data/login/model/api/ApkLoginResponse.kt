package com.example.offiqlresturantapp.data.login.model.api

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "Soap:Envelope", strict = false)
data class ApkLoginResponse @JvmOverloads constructor(
    @field:Element(name = "Login_Result", required = false)
    @param:Element(name = "Login_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val apkLoginResult: APKLoginResult? = null
)

@Root(name = "Login_Result", strict = false)
data class APKLoginResult @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)