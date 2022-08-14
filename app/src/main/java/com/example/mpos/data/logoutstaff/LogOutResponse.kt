package com.example.mpos.data.logoutstaff

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class LogOutResponse (
    @field:Element(name = "LogoutStaff_Result", required = false)
    @param:Element(name = "LogoutStaff_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: LogOutResponseBody? = null

)

@Root(name = "LogoutStaff_Result", strict = false)
data class LogOutResponseBody (
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)
