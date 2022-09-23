package com.example.mpos.ui.oderconfirm.repo.occupied

import android.util.Log
import com.example.mpos.api.occupied.OccupiedTableApi
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.data.occupied.OccupiedTableRequest
import com.example.mpos.data.occupied.json.OccupiedTable
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
import com.example.mpos.utils.deserializeFromJson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class OccupiedTableRepository(retrofit: Retrofit) {

    private val api = buildApi<OccupiedTableApi>(retrofit)

    private val err = "Cannot Get Menu Item for this Table"

    fun getOccupiedMenuApi(request: OccupiedTableRequest) = flow {
        emit(ApisResponse.Loading("Please Wait"))
        val data = try {
            val response = api.sendPostRequestApi(request)
            if (response.isSuccessful) {
                deserializeFromJson<OccupiedTable>(response.body()?.occupiedTableBody?.value)?.let {
                    Log.i("ANUJ ", "getOccupiedMenuApi: $it")
                    return@let getFoodItem(it)
                } ?: ApisResponse.Error(err + "Cannot convert it", null)
            } else {
                ApisResponse.Error(err, null)
            }

        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)

    private fun getFoodItem(occupiedTable: OccupiedTable): ApisResponse.Success<FoodItemList> {
        val mutableList = mutableListOf<ItemMasterFoodItem>()
        occupiedTable.forEach {
            mutableList.add(
                ItemMasterFoodItem(
                    foodAmt = it.foodAmt.toInt(), itemMaster = ItemMaster(
                        0,
                        barcode = it.itemMaster.barcode,
                        itemDescription = it.itemMaster.itemDescription,
                        itemCode = it.itemMaster.itemCode,
                        salePrice = ListOfFoodItemToSearchAdaptor.setPrice(it.itemMaster.salePrice).toString(),
                        uOM = it.itemMaster.uOM,
                        itemCategory = it.itemMaster.itemCategory,
                        itemName = it.itemMaster.itemName,
                        decimalAllowed = false.toString()
                    ), foodQty = it.foodQty
                )
            )
        }
        return  ApisResponse.Success(FoodItemList(mutableList))
    }


}