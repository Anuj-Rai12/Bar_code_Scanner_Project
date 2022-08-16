package com.example.mpos.ui.deals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.deals.DealRequestBody
import com.example.mpos.data.deals.DealsRequest
import com.example.mpos.di.RetrofitInstance
import com.example.mpos.ui.deals.repo.DealsRepository
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.Events
import com.example.mpos.utils.RestaurantSingletonCls
import com.example.mpos.utils.isNetworkAvailable
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}