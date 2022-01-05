package com.example.offiqlresturantapp.othermodel

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = "response", strict = false)
data class ResponseOp @JvmOverloads constructor(

    @field:Element(name = "msg", required = false)
    @param:Element(name = "msg", required = false)
    @field:Path("result")
    @param:Path("result")
    val msg: Msg?=null,

    @field:Element(name = "status", required = false)
    @param:Element(name = "status", required = false)
    @field:Path("result")
    @param:Path("result")
    val status: Msg?=null,

    @field:Element(name = "stid")
    @param:Element(name = "stid")
    val stId: String? = null

    /*@field:Element(name = "object", required = false)
    @param:Element(name = "object", required = false)
    @field:Path("result/msg")
    @param:Path("result/msg")
    val msgObject: Object? = null*/
)


@Root(name = "msg", strict = false)
data class Msg @JvmOverloads constructor(

    @field:Element(name = "text")
    @param:Element(name = "text")
    val text: String,

    @field:Element(name = "code")
    @param:Element(name = "code")
    val code: String,

    @field:Element(name = "type")
    @param:Element(name = "type")
    val type: String,

    @field:Element(name = "object", required = false)
    @param:Element(name = "object", required = false)
    val msgObject: Object? = null
)

/*@field:Path("object")
@param:Path("object")*/
@Root(name = "object", strict = false)
data class Object @JvmOverloads constructor(

    @field:Element(name = "type")
    @param:Element(name = "type")
    val type: String? = null,

    @field:Element(name = "value")
    @param:Element(name = "value")
    val value: String? = null
)
