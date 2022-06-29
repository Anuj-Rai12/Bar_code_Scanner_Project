package com.example.offiqlresturantapp.data.barcode.request

import com.example.offiqlresturantapp.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Envelope, strict = false)
data class BarCodeRequest @JvmOverloads constructor(
    @field:Element(name = "ScanAndFindITEM", required = false)
    @param:Element(name = "ScanAndFindITEM", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: BarCodeRequestBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns

)

@Root(name = "ScanAndFindITEM", strict = false)
data class BarCodeRequestBody(

    @field:Element(name = "storeNo")
    @param:Element(name = "storeNo")
    val storeNo: String? = null,

    @field:Element(name = "barcodeInput")
    @param:Element(name = "barcodeInput")
    val barcodeInput: String? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)