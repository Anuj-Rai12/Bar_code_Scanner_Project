package com.example.offiqlresturantapp.api.tbl

import com.example.offiqlresturantapp.data.table_info.model.TableInformationRequest
import com.example.offiqlresturantapp.data.table_info.model.TableInformationResponse
import com.example.offiqlresturantapp.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TableInformationApi {

    @Headers("SOAPAction: urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI:TableInformation")
    @POST(AllStringConst.End_point)
    suspend fun getTblInformation(@Body request: TableInformationRequest): Response<TableInformationResponse>
}