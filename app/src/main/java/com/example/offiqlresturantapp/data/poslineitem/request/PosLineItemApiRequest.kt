package com.example.offiqlresturantapp.data.poslineitem.request

import com.example.offiqlresturantapp.utils.AllStringConst
import com.example.offiqlresturantapp.utils.getDate
import org.simpleframework.xml.*


@Root(name = AllStringConst.Envelope, strict = false)
data class PosLineItemApiRequest @JvmOverloads constructor(

    @field:Element(name = "POSLineItems", required = false)
    @param:Element(name = "POSLineItems", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: RequestBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmlns
)


@Root(name = "POSLineItems", strict = false)
data class RequestBody @JvmOverloads constructor(

    @field:Element(name = "itemLineMPOS")
    @param:Element(name = "itemLineMPOS")
    val items: MunItemContainer? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)


@Root(name = "itemLineMPOS", strict = false)
data class MunItemContainer @JvmOverloads constructor(

    @field:ElementList(name = "MPOSLineInsert")
    @param:ElementList(name = "MPOSLineInsert")
    val item: List<MenuItem>? = null

)


@Root(name = "MPOSLineInsert", strict = false)
data class MenuItem @JvmOverloads constructor(

    @field:Element(name = "ItemNo")
    @param:Element(name = "ItemNo")
    val itemNo: String? = null,

    @field:Element(name = "RcptNo")
    @param:Element(name = "RcptNo")
    val receiptNo: String? = null,

    @field:Element(name = "Qty")
    @param:Element(name = "Qty")
    val qty: String? = null,


    @field:Element(name = "SalesType")
    @param:Element(name = "SalesType")
    val saleType: String = "RESTAURANT",

    @field:Element(name = "TransDate")
    @param:Element(name = "TransDate")
    val date: String? = getDate("dd/MM/yy") ?: "10/20/22",

    @field:Element(name = "TransTime")
    @param:Element(name = "TransTime")
    val time: String? = null,

    @field:Element(name = "StoreNo")
    @param:Element(name = "StoreNo")
    val storeNo: String? = null,

    @field:Element(name = "P_FreeText")
    @param:Element(name = "P_FreeText")
    val freeText: String = "",

    @field:Element(name = "P_Price")
    @param:Element(name = "P_Price")
    val price: String? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmlsList
)