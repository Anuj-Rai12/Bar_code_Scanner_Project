package com.fbts.mpos.api.tbl

import com.fbts.mpos.data.table_info.model.TableInformationRequest
import com.fbts.mpos.data.table_info.model.TableInformationResponse
import com.fbts.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TableInformationApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.tblInformation}")
    @POST(AllStringConst.End_point)
    suspend fun getTblInformation(@Body request: TableInformationRequest): Response<TableInformationResponse>
}