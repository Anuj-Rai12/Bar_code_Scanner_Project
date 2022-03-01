package com.example.offiqlresturantapp.ui.searchfood.repo

import android.app.Application
import androidx.room.withTransaction
import com.example.offiqlresturantapp.api.master_sync.ItemMasterSyncApi
import com.example.offiqlresturantapp.data.item_master_sync.ItemMasterSyncRequest
import com.example.offiqlresturantapp.data.item_master_sync.TableInformation
import com.example.offiqlresturantapp.data.item_master_sync.json.ItemMethodSyncJsonResponse
import com.example.offiqlresturantapp.db.RoomDataBaseInstance
import com.example.offiqlresturantapp.utils.buildApi
import com.example.offiqlresturantapp.utils.deserializeFromJson
import com.example.offiqlresturantapp.utils.isNetworkAvailable
import com.example.offiqlresturantapp.utils.networkBoundResource
import kotlinx.coroutines.delay
import retrofit2.Retrofit
import javax.inject.Inject

class SearchFoodRepositoryImpl @Inject constructor(
    private val roomDataBaseInstance: RoomDataBaseInstance,
    private val application: Application,
    retrofit: Retrofit
) : SearchFoodRepository {

    private val api = buildApi<ItemMasterSyncApi>(retrofit)
    private val dao = roomDataBaseInstance.itemDao()

    override fun getItemMasterSync(stateNo: String) = networkBoundResource(
        query = {
            dao.getAllItem()
        },
        fetch = {
            delay(2000)
            val info = api.getItemMasterSync(ItemMasterSyncRequest(TableInformation(stateNo)))
            info.body()?.apkLoginResult?.value?.let {
                return@let deserializeFromJson<ItemMethodSyncJsonResponse>(it)
            }!!.itemMaster
        },
        saveFetchResult = { item ->
            roomDataBaseInstance.withTransaction {
                dao.deleteAllData()
                dao.insertAllItem(item)
            }
        },
        shouldFetch = {
            application.isNetworkAvailable()
        }
    )


/*emit(ApisResponse.Loading("Please Wait.."))
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
        emit(data)*/
}