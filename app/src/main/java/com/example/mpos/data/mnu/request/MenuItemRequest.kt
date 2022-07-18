package com.example.mpos.data.mnu.request


import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class MenuItemRequest @JvmOverloads constructor(

    @field:Element(name = "CategoryMenu", required = false)
    @param:Element(name = "CategoryMenu", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: MenuItemRequestBody? = null,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmlns

)


@Root(name = "CategoryMenu", strict = false)
data class MenuItemRequestBody @JvmOverloads constructor(

    @field:Element(name = "storeID", required = false)
    @param:Element(name = "storeID", required = false)
    val storeID: String? = null,

    @field:Element(name = "salesType", required = false)
    @param:Element(name = "salesType", required = false)
    val saleType: String = AllStringConst.API.RESTAURANT.name,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)
