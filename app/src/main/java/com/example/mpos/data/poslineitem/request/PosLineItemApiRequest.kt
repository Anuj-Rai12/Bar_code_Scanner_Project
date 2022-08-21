package com.example.mpos.data.poslineitem.request

import com.example.mpos.utils.AllStringConst
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

    @field:Element(name = "itemLineMPOS", required = false)
    @param:Element(name = "itemLineMPOS", required = false)
    val items: MunItemContainer? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls
)


@Root(name = "itemLineMPOS", strict = false)
data class MunItemContainer @JvmOverloads constructor(

    @field:ElementList(name = "MPOSLineInsert", inline = true, required = false)
    @param:ElementList(name = "MPOSLineInsert", inline = true, required = false)
    val item: List<MenuItem>? = null

)


@Root(name = "MPOSLineInsert", strict = true)
@org.simpleframework.xml.Order(
    elements = ["ItemNo", "RcptNo",
        "Qty", "SalesType", "TransDate", "TransTime", "StoreNo", "P_FreeText", "P_Price"]
)
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
    val saleType: String? = null,

    @field:Element(name = "TransDate")
    @param:Element(name = "TransDate")
    val date: String? = null,

    @field:Element(name = "TransTime")
    @param:Element(name = "TransTime")
    val time: String? = null,

    @field:Element(name = "StoreNo")
    @param:Element(name = "StoreNo")
    val storeNo: String? = null,

    @field:Element(name = "P_FreeText")
    @param:Element(name = "P_FreeText")
    val freeText: String? = null,

    @field:Element(name = "P_Price")
    @param:Element(name = "P_Price")
    val price: String? = null,


    @field:Element(name = "DealLine")
    @param:Element(name = "DealLine")
    val dealLine: String? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmlsList
)