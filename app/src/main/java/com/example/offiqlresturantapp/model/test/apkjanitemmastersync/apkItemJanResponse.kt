package com.example.offiqlresturantapp.model.test.apkjanitemmastersync

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "Soap:Envelope", strict = false)
data class EnvelopeItemSyncResponse @JvmOverloads constructor(
    @field:Element(name = "ItemMasterSync_Result", required = false)
    @param:Element(name = "ItemMasterSync_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val apkLoginResult: ItemMasterSyncResult? = null
)

@Root(name = "ItemMasterSync_Result", strict = false)
data class ItemMasterSyncResult @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)
