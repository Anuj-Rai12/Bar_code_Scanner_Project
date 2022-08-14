package com.example.mpos.data.logoutstaff

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class LogOutRequest @JvmOverloads constructor(
    @field:Element(name = "LogoutStaff", required = false)
    @param:Element(name = "LogoutStaff", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: LogOutRequestBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns //http://schemas.xmlsoap.org/soap/envelope/
)

@Root(name = "Login", strict = false)
data class LogOutRequestBody @JvmOverloads constructor(

    @field:Element(name = "staffId")
    @param:Element(name = "staffId")
    val staffId: String? = null,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)
