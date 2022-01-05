package com.example.offiqlresturantapp.othermodel

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class RssOp @JvmOverloads constructor(

    @field:Element(name = "channel", required = false)
    @param:Element(name = "channel", required = false)
    val channel: ChannelOp? = null,
)

@Root(name = "channel", strict = false)
data class ChannelOp @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val title: String? = null,

    @field:Element(name = "description")
    @param:Element(name = "description")
    val description: String? = null,


    /*@field:Element(name = "link")
    @param:Element(name = "link")
    val link: String? = null, */
    //List<LinkElement>,

    @field:Element(name = "lastBuildDate")
    @param:Element(name = "lastBuildDate")
    val lastBuildDate: String? = null,

    @field:Element(name = "generator")
    @param:Element(name = "generator")
    val generator: String? = null,

    @field:Element(name = "category")
    @param:Element(name = "category")
    val category: String? = null,

    @field:Element(name = "ttl")
    @param:Element(name = "ttl")
    val ttl: String? = null,

    @field:Element(name = "copyright")
    @param:Element(name = "copyright")
    val copyright: String? = null,

    @field:Element(name = "language")
    @param:Element(name = "language")
    val language: String? = null,

    @field:Element(name = "docs")
    @param:Element(name = "docs")
    val docs: String? = null,

    @field:Element(name = "image", required = false)
    @param:Element(name = "image", required = false)
    val image: ImageOp? = null,

    @field:ElementList(name = "item", inline = true, required = false)
    @param:ElementList(name = "item", inline = true, required = false)
    val item: List<ItemOp>? = null

)

@Root(name = "image", strict = false)
data class ImageOp @JvmOverloads constructor(

    @field:Element(name = "title")
    @param:Element(name = "title")
    val title: String? = null,

    @field:Element(name = "url")
    @param:Element(name = "url")
    val url: String? = null,

    @field:Element(name = "link")
    @param:Element(name = "link")
    val link: String? = null,

    @field:Element(name = "description")
    @param:Element(name = "description")
    val description: String? = null
)

@Root(name = "item", strict = false)
data class ItemOp @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val title: String? = null,            //Description,

    @field:Element(name = "link")
    @param:Element(name = "link")
    val link: String? = null,            //Description,

    @field:Element(name = "description")
    @param:Element(name = "description")
    val description: String? = null,    //Description,

    @field:Element(name = "pubDate")
    @param:Element(name = "pubDate")
    val pub_Date: String? = null,       //Description,

    @field:Element(name = "guid")
    @param:Element(name = "guid")
    val guidOp: String? = null,         //Description,

    @field:Element(name = "category")
    @param:Element(name = "category")
    val category: String? = null,    //Description,
)