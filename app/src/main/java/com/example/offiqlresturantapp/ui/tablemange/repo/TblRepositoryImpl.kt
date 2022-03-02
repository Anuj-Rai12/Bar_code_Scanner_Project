package com.example.offiqlresturantapp.ui.tablemange.repo

import android.app.Application
import androidx.room.withTransaction
import com.example.offiqlresturantapp.api.tbl.TableInformationApi
import com.example.offiqlresturantapp.data.table_info.model.TableInformation
import com.example.offiqlresturantapp.data.table_info.model.TableInformationRequest
import com.example.offiqlresturantapp.data.table_info.model.json.TableInformationJsonResponse
import com.example.offiqlresturantapp.db.RoomDataBaseInstance
import com.example.offiqlresturantapp.utils.buildApi
import com.example.offiqlresturantapp.utils.deserializeFromJson
import com.example.offiqlresturantapp.utils.isNetworkAvailable
import com.example.offiqlresturantapp.utils.networkBoundResource
import retrofit2.Retrofit
import javax.inject.Inject

class TblRepositoryImpl @Inject constructor(
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
    )

}