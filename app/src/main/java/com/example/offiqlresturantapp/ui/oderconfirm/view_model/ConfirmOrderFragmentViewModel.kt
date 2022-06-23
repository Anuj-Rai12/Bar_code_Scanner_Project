package com.example.offiqlresturantapp.ui.oderconfirm.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offiqlresturantapp.data.cofirmDining.ConfirmDiningRequest
import com.example.offiqlresturantapp.data.confirmOrder.ConfirmOrderRequest
import com.example.offiqlresturantapp.data.table_info.model.json.TableDetail
import com.example.offiqlresturantapp.ui.oderconfirm.repo.confirmdining.ConfirmDiningRepositoryImpl
import com.example.offiqlresturantapp.ui.oderconfirm.repo.confirmorder.ConfirmOrderRepositoryImpl
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItemList
import com.example.offiqlresturantapp.ui.searchfood.model.ItemMasterFoodItem
import com.example.offiqlresturantapp.use_case.ConfirmOrderUseCase
import com.example.offiqlresturantapp.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderFragmentViewModel @Inject constructor(
    private val useCase: ConfirmOrderUseCase,
    private val application: Application,
    private val confirmOrderRepository: ConfirmOrderRepositoryImpl,
    private val confirmDiningRepository: ConfirmDiningRepositoryImpl
) : ViewModel() {

    private val _event = MutableLiveData<Events<Map<String, Boolean>>>()
    val event: LiveData<Events<Map<String, Boolean>>>
        get() = _event

    private val _viewDealsList = MutableLiveData<MutableList<ItemMasterFoodItem>>()
    val viewDeals: LiveData<MutableList<ItemMasterFoodItem>>
        get() = _viewDealsList


    private val _orderDining = MutableLiveData<ApisResponse<out Any?>>()
    val orderDining: LiveData<ApisResponse<out Any?>>
        get() = _orderDining

    private val _orderConfirm = MutableLiveData<ApisResponse<out Any?>>()
    val orderConfirm: LiveData<ApisResponse<out Any?>>
        get() = _orderConfirm


    private val _listOfOrder = MutableLiveData<ApisResponse<out List<ItemMasterFoodItem>>>()
    val listOfOrder: LiveData<ApisResponse<out List<ItemMasterFoodItem>>>
        get() = _listOfOrder


    private val _time = MutableLiveData<String>()
    val time: LiveData<String>
        get() = _time

    init {
        if (!application.isNetworkAvailable()) {
            _event.postValue(Events(mapOf("No Internet Connection" to false)))
        }
        getTime()
    }


    fun getOrderList(foodItemList: FoodItemList?) {
        foodItemList?.let { foodItem ->
            _listOfOrder.postValue(ApisResponse.Success(foodItem.foodList))
            return@let
        } ?: _listOfOrder.postValue(ApisResponse.Loading(null))
    }


    fun deleteSwipe(food: ItemMasterFoodItem) {
        _listOfOrder.value?.let {
            Log.i(TAG, "deleteSwipe: $food")
            if (it is ApisResponse.Success) {
                it.data?.let { list ->
                    getOrderItem(food, true)
                    val item = mutableListOf<ItemMasterFoodItem>()
                    item.addAll(list)
                    item.remove(food)
                    if (item.isNotEmpty()) {
                        Log.i(TAG, "deleteSwipe:TEST $item")
                        _listOfOrder.postValue(ApisResponse.Success(item))
                    } else {
                        _listOfOrder.postValue(ApisResponse.Loading(null))
                    }
                }
            }
        }
    }

    fun addUpdateQty(itemRemoved: ItemMasterFoodItem, food: ItemMasterFoodItem) {
        _listOfOrder.value?.let {
            if (it is ApisResponse.Success) {
                it.data?.let { list ->
                    val item = mutableListOf<ItemMasterFoodItem>()
                    item.addAll(list)
                    item.remove(itemRemoved)
                    item.add(food)
                    _listOfOrder.postValue(ApisResponse.Success(item))
                }
            } else {
                val item = mutableListOf<ItemMasterFoodItem>()
                item.add(food)
                _listOfOrder.postValue(ApisResponse.Success(item))
            }
        }
    }


    fun getTbl(tbl: TableDetail) = "${tbl.tableNo}:${tbl.guestNumber}P"


    fun getOrderItem(foodItem: ItemMasterFoodItem, flag: Boolean = false) {
        _viewDealsList.value?.let { myList ->
            val item = myList.find { res -> res.itemMaster.id == foodItem.itemMaster.id }
            if (item != null) {
                myList.remove(foodItem)
            } else {
                if (!flag) {
                    myList.add(foodItem)
                    _event.postValue(Events(mapOf(("Item Selected" to true))))
                }
            }
            _viewDealsList.postValue(myList)
            return@let
        } ?: if (!flag) {
            _viewDealsList.postValue(mutableListOf(foodItem))
        }
    }


    fun getGrandTotal(list: List<ItemMasterFoodItem>?): String {
        var total = 0
        list?.let {
            total = useCase.calGrandTotal(it)
        }
        return "$Rs_Symbol $total"
    }

    private fun getTime(time: String = "HH:mm") {
        viewModelScope.launch {
            useCase.getCurrentDate(time).collectLatest {
                _time.postValue(it)
            }
        }
    }


    fun updateAndLockTbl(confirmDiningRequest: ConfirmDiningRequest) {
        viewModelScope.launch {
            if (application.isNetworkAvailable()) {
                confirmDiningRepository.updateAndLockTbl(confirmDiningRequest).collectLatest {
                    _orderDining.postValue(it)
                }
            } else {
                _event.postValue(Events(mapOf("No Internet Connection" to false)))
            }
        }
    }

    fun saveUserOrderItem(confirmOrderRequest: ConfirmOrderRequest) {
        viewModelScope.launch {
            if (application.isNetworkAvailable()) {
                confirmOrderRepository.saveUserOrderItem(confirmOrderRequest).collectLatest {
                    _orderConfirm.postValue(it)
                }
            } else {
                _event.postValue(Events(mapOf("No Internet Connection" to false)))
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}