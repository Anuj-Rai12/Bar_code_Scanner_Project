package com.example.offiqlresturantapp.ui.searchfood.repo

import android.app.Application
import android.util.Log
import androidx.room.withTransaction
import com.example.offiqlresturantapp.api.master_sync.ItemMasterSyncApi
import com.example.offiqlresturantapp.data.item_master_sync.ItemMasterSyncRequest
import com.example.offiqlresturantapp.data.item_master_sync.TableInformation
import com.example.offiqlresturantapp.data.item_master_sync.json.ItemMethodSyncJsonResponse
import com.example.offiqlresturantapp.db.RoomDataBaseInstance
import com.example.offiqlresturantapp.utils.*
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
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

    override fun getSearchFoodItem(query: String) = channelFlow {
        dao.searchResult(query).collectLatest {
            Log.i(TAG, "getSearchFoodItem: $it")
            send(ApisResponse.Success(it))
        }
    }

    override fun getSearchFoodItem() = channelFlow {
        send(ApisResponse.Loading("Please Wait"))
        dao.getAllItem().collectLatest {
            send(ApisResponse.Success(it))
        }
    }


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