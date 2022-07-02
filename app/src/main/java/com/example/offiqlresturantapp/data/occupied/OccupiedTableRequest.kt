package com.example.offiqlresturantapp.data.occupied

import com.example.offiqlresturantapp.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class OccupiedTableRequest @JvmOverloads constructor(

    @field:Element(name = "OccupiedTableDetail", required = false)
    @param:Element(name = "OccupiedTableDetail", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: RequestBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmlns
)


@Root(name = "OccupiedTableDetail", strict = false)
data class RequestBody @JvmOverloads constructor(

    @field:Element(name = "tableNo", required = false)
    @param:Element(name = "tableNo", required = false)
    val tableNo: String? = null,


    @field:Element(name = "mPOSDoc", required = false)
    @param:Element(name = "mPOSDoc", required = false)
    val receiptNo: String? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)