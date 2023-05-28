package com.example.mpos.data.feedback.feedbck


import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class FeedBackResponse @JvmOverloads constructor(
    @field:Element(name = "FinalFeedbackSend_Result", required = false)
    @param:Element(name = "FinalFeedbackSend_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val body: ResponseFeedBackSend? = null
)

@Root(name = "FinalFeedbackSend_Result", strict = false)
data class ResponseFeedBackSend @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null
)