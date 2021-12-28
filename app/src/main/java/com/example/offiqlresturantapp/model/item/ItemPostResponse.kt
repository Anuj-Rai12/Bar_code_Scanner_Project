package com.example.offiqlresturantapp.model.item

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = true)
data class EnvelopeItemPost(
    @field:Element(name = "Body", required = true)
    val body: BodyPostItem,
)

data class BodyPostItem(
    @field:Element(name = "ItemMasterSync", required = true)
    val itemMasterSync: ItemMasterSyncPost
)

data class ItemMasterSyncPost(
    @field:Element(name = "_xmlns")
    val xmlns: String = ""
)