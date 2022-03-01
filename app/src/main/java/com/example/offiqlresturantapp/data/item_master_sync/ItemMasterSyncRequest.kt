package com.example.offiqlresturantapp.data.item_master_sync

import com.example.offiqlresturantapp.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class ItemMasterSyncRequest @JvmOverloads constructor(

    @field:Element(name = "ItemMasterSync", required = false)
    @param:Element(name = "ItemMasterSync", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: TableInformation? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns
)

@Root(name = "ItemMasterSync", strict = false)
data class TableInformation(

    @field:Element(name = "storeNo")
    @param:Element(name = "storeNo")
    val storeNo: String? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)