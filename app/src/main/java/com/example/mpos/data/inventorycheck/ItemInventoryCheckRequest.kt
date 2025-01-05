package com.example.mpos.data.inventorycheck

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class ItemInventoryCheckRequest @JvmOverloads constructor(

    @field:Element(name = "ItemLiveInventory", required = false)
    @param:Element(name = "ItemLiveInventory", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: InventoryCheck? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns
)

@Root(name = "ItemLiveInventory", strict = false)
data class InventoryCheck(

    @field:Element(name = "stringParam")
    @param:Element(name = "stringParam")
    val storeInfo: String? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmlnsInventroy
)