package com.example.offiqlresturantapp.ui.tablemange.repo

import android.util.Log
import com.example.offiqlresturantapp.api.tbl.TableInformationApi
import com.example.offiqlresturantapp.data.table_info.model.TableInformation
import com.example.offiqlresturantapp.data.table_info.model.TableInformationRequest
import com.example.offiqlresturantapp.data.table_info.model.json.TableInformationJsonResponse
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.TAG
import com.example.offiqlresturantapp.utils.buildApi
import com.example.offiqlresturantapp.utils.deserializeFromJson
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import javax.inject.Inject

class TblRepositoryImpl @Inject constructor(retrofit: Retrofit) : TableRepository {

    private val api = buildApi<TableInformationApi>(retrofit)


    override fun getTblInformation(storeId: String, type: String) = flow {
        emit(ApisResponse.Loading("Please Wait.."))
        kotlinx.coroutines.delay(1000)
        val data = try {
            val info =
                api.getTblInformation(TableInformationRequest(TableInformation(storeId, type)))
            val res = if (info.isSuccessful) {
                info.body()?.apkLoginResult?.value?.let {
                    return@let deserializeFromJson<TableInformationJsonResponse>(it)
                }
            } else {
                Log.i(TAG, "getTblInformation: ${info.errorBody()}")
                null
            }
            ApisResponse.Success(res)
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }

}