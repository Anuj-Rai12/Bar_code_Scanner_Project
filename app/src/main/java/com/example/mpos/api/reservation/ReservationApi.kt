package com.example.mpos.api.reservation

import com.example.mpos.data.reservation.request.AddTableReservationRequest
import com.example.mpos.data.reservation.request.GetTableReservationRequest
import com.example.mpos.data.reservation.response.AddReservationResponse
import com.example.mpos.data.reservation.response.GetTableReservationResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ReservationApi {


    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.addReservation}")
    @POST(AllStringConst.End_point)
    suspend fun addReservationSection( @Body request:AddTableReservationRequest):Response<AddReservationResponse>

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.getReservation}")
    @POST(AllStringConst.End_point)
    suspend fun getReservationSection(@Body requestBody: GetTableReservationRequest):
            Response<GetTableReservationResponse>


}