package com.example.mpos.data.item_master_sync

import com.example.mpos.utils.AllStringConst
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = AllStringConst.Soap_Envelope, strict = false)
data class ItemMasterSyncResponse @JvmOverloads constructor(
    @field:Element(name = "ItemMasterSync_Result", required = false)
    @param:Element(name = "ItemMasterSync_Result", required = false)
    @field:Path("Soap:Body")
    @param:Path("Soap:Body")
    val apkLoginResult: Response? = null
)

@Root(name = "ItemMasterSync_Result", strict = false)
data class Response @JvmOverloads constructor(
    @field:Element(name = "return_value")
    @param:Element(name = "return_value")
    val value: String? = null,
)