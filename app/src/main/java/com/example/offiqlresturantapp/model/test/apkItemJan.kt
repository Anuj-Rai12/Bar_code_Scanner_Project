package com.example.offiqlresturantapp.model.test

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "Envelope",strict = false)
data class EnvelopeItemSync @JvmOverloads constructor(

    @field:Element(name = "ItemMasterSync", required = false)
    @param:Element(name = "ItemMasterSync")
    @field:Path(value = "Body")
    @param:Path(value = "Body")
    val itemMasterSyncJan: ItemMasterSyncJan?=null

    /*@Json(name = "Body")
    val body: Body,

    @Json(name = "_xmlns")
    val xmlns: String*/
)

/*data class Body (
    @Json(name = "ItemMasterSync")
    val itemMasterSync: ItemMasterSync
)*/

@Root(name = "ItemMasterSync",strict = false)
data class ItemMasterSyncJan(

    val xmlns: String?=null
)