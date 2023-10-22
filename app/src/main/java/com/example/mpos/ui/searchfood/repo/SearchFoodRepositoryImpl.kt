package com.example.mpos.ui.searchfood.repo

import android.app.Application
import android.util.Log
import androidx.room.withTransaction
import com.example.mpos.api.crosselling.CrossSellingApi
import com.example.mpos.api.master_sync.ItemMasterSyncApi
import com.example.mpos.data.crosssellingApi.request.CrossSellingRequest
import com.example.mpos.data.crosssellingApi.request.CrossSellingRequestBody
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingJsonResponse
import com.example.mpos.data.item_master_sync.ItemMasterSyncRequest
import com.example.mpos.data.item_master_sync.TableInformation
import com.example.mpos.data.item_master_sync.json.ItemMethodSyncJsonResponse
import com.example.mpos.db.RoomDataBaseInstance
import com.example.mpos.utils.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import retrofit2.Retrofit


class SearchFoodRepositoryImpl constructor(
    private val roomDataBaseInstance: RoomDataBaseInstance,
    private val application: Application,
    retrofit: Retrofit
) : SearchFoodRepository {

    private val api = buildApi<ItemMasterSyncApi>(retrofit)
    private val crossSellingApi = buildApi<CrossSellingApi>(retrofit)
    private val dao = roomDataBaseInstance.itemDao()

    override fun getItemMasterSync(stateNo: String, screenType: String?) =
        networkBoundResource(query = {
            dao.getAllItem()
        }, fetch = {
            val info = api.getItemMasterSync(
                ItemMasterSyncRequest(
                    TableInformation(
                        storeNo = stateNo,
                        screenType = screenType ?: RestaurantSingletonCls.getInstance()
                            .getScreenType()!!
                    )
                )
            )
            //info.body()?.apkLoginResult?.value?.let { //for testing only
            readFromFile(application, "jsonviewer.json")?.let {
                return@let deserializeFromJson<ItemMethodSyncJsonResponse>(it)
            }!!.itemMaster
        }, saveFetchResult = { item ->
            roomDataBaseInstance.withTransaction {
                dao.deleteAllData()
                dao.insertAllItem(item)
            }
        }, shouldFetch = {
            application.isNetworkAvailable()
        }).flowOn(IO)

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

    override fun getCrossSellingResponse(itemCode: String) = flow {
        emit(ApisResponse.Loading("Checking CrossSelling Item.."))
        //U+1F615 😕
        val data = try {
            val api =
                crossSellingApi.crossSellingAPI(CrossSellingRequest(CrossSellingRequestBody(itemNo = itemCode)))
            val response = api.body()
            if (api.isSuccessful) {
                if (response != null) {
                    if (response.body?.returnValue != null) {
                        deserializeFromJson<CrossSellingJsonResponse>(response.body.returnValue)?.let { cross ->
                            return@let if (cross.childItemList.isEmpty()) {
                                ApisResponse.Error(
                                    "CrossSelling item is Empty ${
                                        getEmojiByUnicode(
                                            0x1F615
                                        )
                                    }", null
                                )
                            } else {
                                ApisResponse.Success(cross)
                            }
                        } ?: ApisResponse.Error("Cannot Process CrossSelling Response", null)
                    } else {
                        ApisResponse.Error("No Item Found!! ${getEmojiByUnicode(0x1F615)}", null)
                    }
                } else {
                    ApisResponse.Error(
                        "Cannot Found the Response!! ${getEmojiByUnicode(0x1F615)}", null
                    )
                }
            } else {
                ApisResponse.Error("Oops Something went Wrong", null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


}