package com.example.offiqlresturantapp.model.test

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "Envelope",strict = false)
data class EnvelopeItemSync @JvmOverloads constructor(

    @field:Element(name = "ItemMasterSync", required = false)
    @param:Element(name = "ItemMasterSync")
    @field:Path(value = "Body")
    @param:Path(value = "Body")
    val itemMasterSyncJan: ItemMasterSyncJan?=null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xml: String = "http://schemas.xmlsoap.org/soap/envelope/"
)

@Root(name = "ItemMasterSync",strict = false)
data class ItemMasterSyncJan(
    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = "urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI"
)