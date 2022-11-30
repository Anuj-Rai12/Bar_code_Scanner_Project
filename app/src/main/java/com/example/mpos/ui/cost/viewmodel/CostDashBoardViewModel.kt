package com.example.mpos.ui.cost.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.billing.conifrm_billing.ConfirmBillingRequest
import com.example.mpos.data.billing.printInvoice.request.PrintInvoiceRequest
import com.example.mpos.data.billing.send_billing_to_edc.ScanBillingToEdcRequest
import com.example.mpos.data.checkBillingStatus.CheckBillingStatusRequest
import com.example.mpos.data.costestimation.request.CostEstimation
import com.example.mpos.dataStore.UserSoredData
import com.example.mpos.di.RetrofitInstance
import com.example.mpos.ui.cost.repo.CostDashBoardRepository
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.Events
import com.example.mpos.utils.RestaurantSingletonCls
import com.example.mpos.utils.isNetworkAvailable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CostDashBoardViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application
    private var repository: CostDashBoardRepository? = null
    private val userSoredData = UserSoredData(application)

    private val _events = MutableLiveData<Events<String>>()
    val event: LiveData<Events<String>>
        get() = _events


    private val _costEstimationResponse = MutableLiveData<ApisResponse<out String?>>()
    val costEstimationResponse: LiveData<ApisResponse<out String?>>
        get() = _costEstimationResponse


    private val _sendBillingToEdc = MutableLiveData<ApisResponse<out String?>>()
    val sendBillingToEdc: LiveData<ApisResponse<out String?>>
        get() = _sendBillingToEdc


    private val _printBillInvoice = MutableLiveData<ApisResponse<out Any?>>()
    val printBillInvoice: LiveData<ApisResponse<out Any?>>
        get() = _printBillInvoice


    private val _checkBillingStatus = MutableLiveData<ApisResponse<out String?>>()
    val checkBillingStatus: LiveData<ApisResponse<out String?>>
        get() = _checkBillingStatus

    private val _confirmBillingResponse = MutableLiveData<ApisResponse<out String?>>()
    val confirmBillingResponse: LiveData<ApisResponse<out String?>>
        get() = _confirmBillingResponse

    init {

        repository =
            CostDashBoardRepository(
                RetrofitInstance.getInstance(
                    "UseLess String..",
                    "UseLess BaseUrl"
                ).getRetrofit()
            )


        viewModelScope.launch {
            userSoredData.read.collectLatest {
                Log.i("CONFIRM_ORDER", " COST Dash Bord USERID_CONFIRM_ORDER: USER IS NULL OR EMPTY -> ${RestaurantSingletonCls.getInstance().getUserId().isNullOrEmpty()}")
                Log.i("CONFIRM_ORDER", " COST Dash Bord  USERID_CONFIRM_ORDER: STORE IS NULL OR EMPTY -> ${RestaurantSingletonCls.getInstance().getStoreId().isNullOrEmpty()}")

                if (RestaurantSingletonCls.getInstance().getUserId().isNullOrEmpty())
                    RestaurantSingletonCls.getInstance().setUserID(it.userID!!)
                if (RestaurantSingletonCls.getInstance().getStoreId().isNullOrEmpty())
                    RestaurantSingletonCls.getInstance().setStoreId(it.storeNo!!)
            }
        }

    }


    fun init() {
        _confirmBillingResponse.postValue(null)
        _sendBillingToEdc.postValue(null)
        _printBillInvoice.postValue(null)
        _checkBillingStatus.postValue(null)
    }

    fun getCostEstimation(request: CostEstimation) {
        if (!app.isNetworkAvailable()) {
            _events.postValue(Events("No Internet Connection Found!!"))
            return
        }
        viewModelScope.launch {
            repository?.getCostEstimationParams(request)?.collectLatest {
                _costEstimationResponse.postValue(it)
            } ?: _events.postValue(Events("Oops Repository is Not Set Up!!"))
        }

    }


    fun confirmBilling(request: ConfirmBillingRequest) {
        if (!app.isNetworkAvailable()) {
            _events.postValue(Events("No Internet Connection Found!!"))
            return
        }
        viewModelScope.launch {
            repository?.confirmBilling(request)?.collectLatest {
                _confirmBillingResponse.postValue(it)
            } ?: _events.postValue(Events("Oops Repository is Not Set Up!!"))
        }

    }


    fun scanBillingRequest(request: ScanBillingToEdcRequest) {
        if (!app.isNetworkAvailable()) {
            _events.postValue(Events("No Internet Connection Found!!"))
            return
        }
        viewModelScope.launch {
            repository?.sendBillToEdc(request)?.collectLatest {
                _sendBillingToEdc.postValue(it)
            } ?: _events.postValue(Events("Oops Repository is Not Set Up!!"))
        }

    }


    fun checkBillingStatus(request: CheckBillingStatusRequest) {
        if (!app.isNetworkAvailable()) {
            _events.postValue(Events("No Internet Connection Found!!"))
            return
        }
        viewModelScope.launch {
            repository?.checkBillStatus(request)?.collectLatest {
                _checkBillingStatus.postValue(it)
            } ?: _events.postValue(Events("Oops Repository is Not Set Up!!"))
        }

    }


    fun getPrintBillInvoiceResponse(request: PrintInvoiceRequest) {
        if (!app.isNetworkAvailable()) {
            _events.postValue(Events("No Internet Connection Found!!"))
            return
        }
        viewModelScope.launch {
            repository?.getPrintInvoice(request)?.collectLatest {
                _printBillInvoice.postValue(it)
            } ?: _events.postValue(Events("Oops Repository is Not Set Up!!"))
        }
    }


    fun addError(msg: String) {
        _events.postValue(Events(msg))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}