package com.example.mpos.data.testconnection.api

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class TestingConnectionRequest @JvmOverloads constructor(

    @field:Element(name = "ValidateStoreNo", required = false)
    @param:Element(name = "ValidateStoreNo", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: RequestConnectionBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns

)


@Root(name = "ValidateStoreNo", strict = false)
data class RequestConnectionBody @JvmOverloads constructor(

    @field:Element(name = "storeNo")
    @param:Element(name = "storeNo")
    val storeNo: String? = null,


    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls

)