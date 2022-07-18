package com.example.mpos.data.mnu.response


import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class MenuItemResponse @JvmOverloads constructor(

    @field:Element(name = "CategoryMenu_Result", required = false)
    @param:Element(name = "CategoryMenu_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val categoryMenuResult: Response? = null
)

@Root(name = "CategoryMenu_Result", strict = false)
data class Response @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null
)
