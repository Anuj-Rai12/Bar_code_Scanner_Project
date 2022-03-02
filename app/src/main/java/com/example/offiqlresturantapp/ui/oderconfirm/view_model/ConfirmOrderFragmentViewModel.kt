package com.example.offiqlresturantapp.ui.oderconfirm.view_model

import android.app.Application
import androidx.lifecycle.*
import com.example.offiqlresturantapp.data.table_info.model.json.TableDetail
import com.example.offiqlresturantapp.data.table_info.model.json.TblStatus
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItemList
import com.example.offiqlresturantapp.ui.searchfood.model.ItemMasterFoodItem
import com.example.offiqlresturantapp.use_case.ConfirmOrderUseCase
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.Events
import com.example.offiqlresturantapp.utils.Rs_Symbol
import com.example.offiqlresturantapp.utils.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderFragmentViewModel @Inject constructor(
    private val useCase: ConfirmOrderUseCase,
    application: Application
) : ViewModel() {

    private val _event = MutableLiveData<Events<String>>()
    val event: LiveData<Events<String>>
        get() = _event

    private val _viewDealsList = MutableLiveData<MutableList<ItemMasterFoodItem>>()
    val viewDeals: LiveData<MutableList<ItemMasterFoodItem>>
        get() = _viewDealsList


    private val _listOfOrder = MutableLiveData<ApisResponse<out List<ItemMasterFoodItem>>>()
    val listOfOrder: LiveData<ApisResponse<out List<ItemMasterFoodItem>>>
        get() = _listOfOrder


    private val _time = MutableLiveData<String>()
    val time: LiveData<String>
        get() = _time

    init {
        if (!application.isNetworkAvailable()) {
            _event.postValue(Events("No Internet Connection"))
        }
        getTime()
    }


    fun getOrderList(foodItemList: FoodItemList?) {
        foodItemList?.let { foodItem ->
            _listOfOrder.postValue(ApisResponse.Success(foodItem.foodList))
            return@let
        } ?: _listOfOrder.postValue(ApisResponse.Loading(null))
    }


    fun deleteSwipe(item: ItemMasterFoodItem) {
        _viewDealsList.value?.let {
            it.remove(item)
            _listOfOrder.postValue(ApisResponse.Success(it))
        }
    }


    fun getTbl(tbl: TableDetail)= "${tbl.tableNo}:${tbl.guestNumber}P"



    fun getOrderItem(foodItem: ItemMasterFoodItem) {
        _viewDealsList.value?.let { myList ->
            if (myList.contains(foodItem)) {
                myList.remove(foodItem)
            } else {
                myList.add(foodItem)
            }
            _event.postValue(Events("Item Selected"))
            _viewDealsList.postValue(myList)
            return@let
        } ?: _viewDealsList.postValue(mutableListOf(foodItem))
    }


    fun getGrandTotal(list: FoodItemList?): String {
        val tol = list?.let {
            return@let useCase.calGrandTotal(it.foodList)
        } ?: 0

        return "$Rs_Symbol $tol"
    }

    private fun getTime(time: String = "HH:mm") {
        viewModelScope.launch {
            useCase.getCurrentDate(time).collectLatest {
                _time.postValue(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}