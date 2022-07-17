package com.example.mpos.ui.tablemange.repo

import android.app.Application
import androidx.room.withTransaction
import com.example.mpos.api.tbl.TableInformationApi
import com.example.mpos.data.table_info.model.TableInformation
import com.example.mpos.data.table_info.model.TableInformationRequest
import com.example.mpos.data.table_info.model.json.TableInformationJsonResponse
import com.example.mpos.db.RoomDataBaseInstance
import com.example.mpos.utils.buildApi
import com.example.mpos.utils.deserializeFromJson
import com.example.mpos.utils.isNetworkAvailable
import com.example.mpos.utils.networkBoundResource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class TblRepositoryImpl  constructor(
    private val roomDataBaseInstance: RoomDataBaseInstance,
    private val application: Application,
    retrofit: Retrofit
) : TableRepository {

    private val api = buildApi<TableInformationApi>(retrofit)
    private val dao = roomDataBaseInstance.tblDao()

    override fun getTblInformation(storeId: String, type: String) = networkBoundResource(
        query = {
            dao.getAllItem()
        },
        fetch = {
            val info =
                api.getTblInformation(TableInformationRequest(TableInformation(storeId, type)))
            info.body()?.apkLoginResult?.value?.let {
                return@let deserializeFromJson<TableInformationJsonResponse>(it)
            }!!.tableDetails
        },
        saveFetchResult = {
            roomDataBaseInstance.withTransaction {
                dao.deleteAllData()
                dao.insertAllItem(it)
            }
        }, shouldFetch = {
            application.isNetworkAvailable()
        }
    ).flowOn(IO)

}