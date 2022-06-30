package com.example.offiqlresturantapp.ui.oderconfirm.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.offiqlresturantapp.data.cofirmDining.ConfirmDiningRequest
import com.example.offiqlresturantapp.data.confirmOrder.ConfirmOrderRequest
import com.example.offiqlresturantapp.data.table_info.model.json.TableDetail
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.di.RetrofitInstance
import com.example.offiqlresturantapp.ui.oderconfirm.repo.confirmdining.ConfirmDiningRepositoryImpl
import com.example.offiqlresturantapp.ui.oderconfirm.repo.confirmorder.ConfirmOrderRepositoryImpl
import com.example.offiqlresturantapp.ui.oderconfirm.repo.posline.PosLineRepository
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItemList
import com.example.offiqlresturantapp.ui.searchfood.model.ItemMasterFoodItem
import com.example.offiqlresturantapp.use_case.ConfirmOrderUseCase
import com.example.offiqlresturantapp.utils.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ConfirmOrderFragmentViewModel constructor(
    application: Application
) : AndroidViewModel(application) {


    private val useCase = ConfirmOrderUseCase()
    private val app = application
    private val userSoredData = UserSoredData(application)
    private lateinit var confirmOrderRepository: ConfirmOrderRepositoryImpl
    private lateinit var confirmDiningRepository: ConfirmDiningRepositoryImpl
    private lateinit var posLineRepository: PosLineRepository

    private var storeNo = ""

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


    private val _postLine = MutableLiveData<Pair<String, ApisResponse<out String>>>()
    val postLine: LiveData<Pair<String, ApisResponse<out String>>>
        get() = _postLine


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

        viewModelScope.launch {
            userSoredData.readBase.collectLatest {
                storeNo = it.storeId
                val auth = AllStringConst.getAuthHeader(genToken("${it.userId}:${it.passId}"))
                val retrofit = RetrofitInstance.getInstance(auth = auth, baseUrl = it.baseUrl)
                confirmOrderRepository = ConfirmOrderRepositoryImpl(
                    retrofit = retrofit.getRetrofit()
                )
                confirmDiningRepository =
                    ConfirmDiningRepositoryImpl(retrofit = retrofit.getRetrofit())

                posLineRepository = PosLineRepository(retrofit.getRetrofit())
            }
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
        if (!this::confirmDiningRepository.isInitialized) {
            _event.postValue(Events(mapOf("Unknown Error" to false)))
            return
        }

        viewModelScope.launch {
            if (app.isNetworkAvailable()) {
                confirmDiningRepository.updateAndLockTbl(confirmDiningRequest).collectLatest {
                    _orderDining.postValue(it)
                }
            } else {
                _event.postValue(Events(mapOf("No Internet Connection" to false)))
            }
        }
    }

    fun saveUserOrderItem(confirmOrderRequest: ConfirmOrderRequest) {
        if (!this::confirmOrderRepository.isInitialized) {
            _event.postValue(Events(mapOf("Unknown Error" to false)))
            return
        }
        viewModelScope.launch {
            if (app.isNetworkAvailable()) {
                confirmOrderRepository.saveUserOrderItem(confirmOrderRequest).collectLatest {
                    _orderConfirm.postValue(it)
                }
            } else {
                _event.postValue(Events(mapOf("No Internet Connection" to false)))
            }
        }
    }

    fun postLineUrl(receipt: String, item: List<ItemMasterFoodItem>) {

        if (item.isEmpty()) {
            _event.postValue(Events(mapOf("Please Add The Menu Item!!" to false)))
            return
        }

        if (!this::posLineRepository.isInitialized) {
            _event.postValue(Events(mapOf("Unknown Error" to false)))
            return
        }

        val time = _time.value?.let {
            return@let it.substring(0, it.length - 2)
        } ?: "10:20"

        viewModelScope.launch {
            val postLineInstance = async(context = IO, start = CoroutineStart.LAZY) {
                useCase.getPosLineRequest(
                    receipt = receipt,
                    item = item,
                    time = time,
                    storeNo = storeNo
                )
            }

            if (app.isNetworkAvailable()) {
                posLineRepository.getPosLineResponse(postLineInstance.await()).collectLatest {
                    _postLine.postValue(Pair(receipt, it))
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