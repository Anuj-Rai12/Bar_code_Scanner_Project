package com.example.mpos.ui.cost.repo

import com.example.mpos.api.confirmDining.ConfirmDiningApi
import com.example.mpos.data.costestimation.request.CostEstimation
import com.example.mpos.ui.menu.repo.MenuRepository
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class CostDashBoardRepository(retrofit: Retrofit) {

    private val api = buildApi<ConfirmDiningApi>(retrofit)

    fun getCostEstimationParams(request: CostEstimation) = flow {
        emit(ApisResponse.Loading("Uploading the Item.."))
        val data = try {
            val response = api.setPostCostEstimationApi(request = request)
            if (response.isSuccessful) {
                response.body()?.let { estimation ->
                    if (!estimation.body?.errorFound.toBoolean() && estimation.body?.returnValue.toBoolean()) {
                        ApisResponse.Success(null)
                    } else {
                        ApisResponse.Error(
                            estimation.body?.errorText ?: "Cannot Upload the menu item", null
                        )
                    }
                } ?: ApisResponse.Error(MenuRepository.nullError, null)
            } else {
                ApisResponse.Error(MenuRepository.err, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)

}