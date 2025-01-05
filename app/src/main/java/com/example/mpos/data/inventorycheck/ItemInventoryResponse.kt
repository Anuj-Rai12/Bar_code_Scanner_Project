package com.example.mpos.data.inventorycheck

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class ItemInventoryResponse @JvmOverloads constructor(
    @field:Element(name = "ItemLiveInventory_Result", required = false)
    @param:Element(name = "ItemLiveInventory_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val apkLoginResult: Response? = null
)

@Root(name = "ItemLiveInventory_Result", strict = false)
data class Response @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)