package com.example.offiqlresturantapp.ui.searchfood.repo

import android.util.Log
import com.example.offiqlresturantapp.api.master_sync.ItemMasterSyncApi
import com.example.offiqlresturantapp.data.item_master_sync.ItemMasterSyncRequest
import com.example.offiqlresturantapp.data.item_master_sync.TableInformation
import com.example.offiqlresturantapp.data.item_master_sync.json.ItemMethodSyncJsonResponse
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.TAG
import com.example.offiqlresturantapp.utils.buildApi
import com.example.offiqlresturantapp.utils.deserializeFromJson
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import javax.inject.Inject

class SearchFoodRepositoryImpl @Inject constructor(retrofit: Retrofit) : SearchFoodRepository {

    private val api = buildApi<ItemMasterSyncApi>(retrofit)

    override fun getItemMasterSync(stateNo: String) = flow {
        emit(ApisResponse.Loading("Please Wait.."))
        val data = try {
            val info = api.getItemMasterSync(ItemMasterSyncRequest(TableInformation(stateNo)))
            val response = if (info.isSuccessful) {
                info.body()?.apkLoginResult?.value?.let {
                    return@let deserializeFromJson<ItemMethodSyncJsonResponse>(it)
                }
            } else {
                Log.i(TAG, "getItemMasterSync: ${info.message()}")
                null
            }

            ApisResponse.Success(response)

        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }


}