package com.example.mpos.ui.reservation.repo

import com.example.mpos.api.reservation.ReservationApi
import com.example.mpos.data.reservation.request.AddTableReservationRequest
import com.example.mpos.data.reservation.request.GetTableReservationRequest
import com.example.mpos.data.reservation.response.json.GetReservationResponse
import com.example.mpos.data.reservation.response.json.addRequest.AddReservationJsonResponse
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
import com.example.mpos.utils.deserializeFromJson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class TableReservationRepository(retrofit: Retrofit) {

    val api = buildApi<ReservationApi>(retrofit)

    private val err = "Oops Something Went Wrong!!"
    private val errMsg = "Cannot load a Data"

    fun getReservationTable(request: GetTableReservationRequest) = flow {
        emit(ApisResponse.Loading("Loading Information"))
        val data = try {
            val response = api.getReservationSection(request)
            if (response.isSuccessful) {
                deserializeFromJson<GetReservationResponse>(response.body()?.responseForBody?.value)?.let { res ->
                    ApisResponse.Success(res)
                } ?: ApisResponse.Error(errMsg, null)
            } else {
                ApisResponse.Error(err, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


    fun addReservationItem(request: AddTableReservationRequest) = flow {
        emit(ApisResponse.Loading("Making Reservation."))
        val data = try {
            val response = api.addReservationSection(request)
            if (response.isSuccessful) {
                deserializeFromJson<AddReservationJsonResponse>(response.body()?.responseForBody?.value)?.let { res ->
                    ApisResponse.Success(res)
                } ?: ApisResponse.Error(errMsg, null)
            } else {
                ApisResponse.Error(err, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


}