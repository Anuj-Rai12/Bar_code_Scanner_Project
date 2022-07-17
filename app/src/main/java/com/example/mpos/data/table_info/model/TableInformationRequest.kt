package com.example.mpos.data.table_info.model

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class TableInformationRequest @JvmOverloads constructor(

    @field:Element(name = "TableInformation", required = false)
    @param:Element(name = "TableInformation", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: TableInformation? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns
)

@Root(name = "TableInformation", strict = false)
data class TableInformation(

    @field:Element(name = "storeNo")
    @param:Element(name = "storeNo")
    val storeNo: String? = null,

    @field:Element(name = "salesType")
    @param:Element(name = "salesType")
    val salesType: String? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)
