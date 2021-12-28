package com.example.offiqlresturantapp.model.item

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
data class ItemEnvelope(
    @Element(name = "Body")
    val body: BodyResponse,

    @Element(name = "_xmlns:Soap")
    val xmlnsSoap: String,

    @Element(name = "__prefix")
    val prefix: String
)

data class BodyResponse(
    @Element(name = "ItemMasterSync_Result")
    val itemMasterSyncResult: ItemMasterSyncResult,

    @Element(name = "__prefix")
    val prefix: String
)

data class ItemMasterSyncResult(
    @Element(name = "return_value")
    val returnValue: String,

    @Element(name = "_xmlns")
    val xmlns: String
)