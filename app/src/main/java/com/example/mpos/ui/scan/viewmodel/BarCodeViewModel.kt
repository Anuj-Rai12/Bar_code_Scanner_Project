package com.example.mpos.ui.scan.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.barcode.request.BarCodeRequest
import com.example.mpos.data.barcode.request.BarCodeRequestBody
import com.example.mpos.dataStore.UserSoredData
import com.example.mpos.di.RetrofitInstance
import com.example.mpos.ui.scan.repo.BarCodeRepository
import com.example.mpos.utils.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BarCodeViewModel(application: Application) : AndroidViewModel(application) {
    private val userSoredData = UserSoredData(application)

    private lateinit var repository: BarCodeRepository

    private val _event = MutableLiveData<Events<ApisResponse<out Any>>>()
    val events: LiveData<Events<ApisResponse<out Any>>>
        get() = _event
    private val app = application
    private val _barCodeResponse = MutableLiveData<Events<ApisResponse<out Any>>>()
    val barCodeResponse: LiveData<Events<ApisResponse<out Any>>>
        get() = _barCodeResponse
    private var storeNo = "PER002"

    init {
        viewModelScope.launch {
            userSoredData.readBase.collectLatest { res ->
                if (checkFieldValue(res.passId) || checkFieldValue(res.userId) || checkFieldValue(
                        res.baseUrl
                    ) || checkFieldValue(res.storeId)
                ) {
                    Log.i(TAG, "ErrorBarCode: aLL Value aRE nULL")
                } else {
                    storeNo = res.storeId
                    val auth = AllStringConst.getAuthHeader(genToken("${res.userId}:${res.passId}"))
                    val retrofit = RetrofitInstance.getInstance(auth, res.baseUrl).getRetrofit()
                    repository = BarCodeRepository(retrofit)
                }
            }
        }
    }

    fun checkForItemItem(itemCode: String) {
        if (!app.isNetworkAvailable()) {
            _event.postValue(Events(ApisResponse.Error("No Internet Connection found", null)))
            return
        }
        if (!this::repository.isInitialized) {
            _event.postValue(Events(ApisResponse.Error("Unknown Error", null)))
            return
        }
        val response =
            BarCodeRequest(body = BarCodeRequestBody(storeNo = storeNo, barcodeInput = itemCode))
        viewModelScope.launch {
            repository.getBarCodeResponse(response).collectLatest {
                _barCodeResponse.postValue((Events(it)))
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}