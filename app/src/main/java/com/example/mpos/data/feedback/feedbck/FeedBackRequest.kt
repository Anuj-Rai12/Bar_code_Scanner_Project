package com.example.mpos.data.feedback.feedbck

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


@Root(name = AllStringConst.Envelope, strict = false)
data class FeedBackRequest @JvmOverloads constructor(

    @field:Element(name = "FinalFeedbackSend", required = false)
    @param:Element(name = "FinalFeedbackSend", required = false)
    @field:Path("Body")
    @param:Path("Body")
    val body: FinalFeedbackSendBody? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlAttr: String = AllStringConst._xmlns

)

@Root(name = "FinalFeedbackSend", strict = false)
data class  FinalFeedbackSendBody @JvmOverloads constructor(
    @field:Element(name = "invoiceStatus")
    @param:Element(name = "invoiceStatus")
    val invoiceStatus: String? = null,

    @field:Attribute(name = "xmlns")
    @param:Attribute(name = "xmlns")
    val xmlns: String = AllStringConst._xmls,
)