package com.example.mpos.ui.deals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.deals.DealRequestBody
import com.example.mpos.data.deals.DealsRequest
import com.example.mpos.data.deals.scan_and_find_deals.ScanAndFindDealsRequest
import com.example.mpos.data.deals.scan_and_find_deals.ScanAndFindDealsRequestBody
import com.example.mpos.di.RetrofitInstance
import com.example.mpos.ui.deals.repo.DealsRepository
import com.example.mpos.utils.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DealsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application

    private val _event = MutableLiveData<Events<String>>()
    val event: LiveData<Events<String>>
        get() = _event

    private val storeId = RestaurantSingletonCls.getInstance().getStoreId()
    fun addError(msg: String) = _event.postValue(Events(msg))
    private lateinit var repository: DealsRepository

    private val _dealsResponse = MutableLiveData<ApisResponse<out Any>>()
    val dealsResponse: LiveData<ApisResponse<out Any>>
        get() = _dealsResponse

    //Get Deals Api
    private val _dealItemResponse = MutableLiveData<ApisResponse<out Any>>()
    val dealItemResponse: LiveData<ApisResponse<out Any>>
        get() = _dealItemResponse


    init {
        if (!app.isNetworkAvailable()) {
            addError("No Internet Connection Found!!")
        }
        val retrofit =
            RetrofitInstance.getInstance("useless auth", "https://www.kid.com").getRetrofit()
        repository = DealsRepository(retrofit)
    }

    fun getDeals() {
        if (!app.isNetworkAvailable()) {
            addError("No Internet Connection Found!!")
            return
        }
        if (!this::repository.isInitialized || storeId.isNullOrEmpty()) {
            addError("Failed to Set up Session")
            return
        }
        viewModelScope.launch {
            repository.getDealsResponse(DealsRequest(DealRequestBody(storeId))).collectLatest {
                _dealsResponse.postValue(it)
            }
        }
    }




    fun getScanDealApi(dealCode: String) {
        if (!app.isNetworkAvailable()) {
            addError("No Internet Connection found!!")
            return
        }
        if (checkFieldValue(dealCode)) {
            addError("Invalid Deal Code")
            return
        }


        viewModelScope.launch {
            repository.getScanDealsApiResponse(
                ScanAndFindDealsRequest(
                    ScanAndFindDealsRequestBody(
                        dealCode
                    )
                )
            ).collectLatest {
                _dealItemResponse.postValue(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}