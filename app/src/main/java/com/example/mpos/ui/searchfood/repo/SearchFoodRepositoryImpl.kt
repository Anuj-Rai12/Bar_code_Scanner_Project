package com.example.mpos.ui.searchfood.repo

import android.app.Application
import android.util.Log
import androidx.room.withTransaction
import com.example.mpos.api.master_sync.ItemMasterSyncApi
import com.example.mpos.data.item_master_sync.ItemMasterSyncRequest
import com.example.mpos.data.item_master_sync.TableInformation
import com.example.mpos.data.item_master_sync.json.ItemMethodSyncJsonResponse
import com.example.mpos.db.RoomDataBaseInstance
import com.example.mpos.utils.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit


class SearchFoodRepositoryImpl constructor(
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
            val info = api.getItemMasterSync(
                ItemMasterSyncRequest(
                    TableInformation(
                        storeNo = stateNo,
                        screenType = RestaurantSingletonCls.getInstance().getScreenType()!!
                    )
                )
            )
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
    ).flowOn(IO)

    override fun getSearchFoodItem(query: String) = channelFlow {
        dao.searchResult(query).collectLatest {
            Log.i(TAG, "getSearchFoodItem: $it")
            send(ApisResponse.Success(it))
        }
    }

    override fun getSearchFoodItem() = channelFlow {
        send(ApisResponse.Loading(listOf()))
        dao.getAllItem().collectLatest {
            send(ApisResponse.Success(it))
        }
    }


}