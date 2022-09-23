package com.example.mpos.utils.print.recpit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.confirmOrder.response.json.PrintReceiptInfo
import com.example.mpos.utils.ApisResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrintViewModel(application: Application) : AndroidViewModel(application) {


    private val _isPrinterConnected=MutableLiveData<ApisResponse<String>>()
    val isPrinterConnected: LiveData<ApisResponse<String>>
    get() = _isPrinterConnected

    private val _doPrinting=MutableLiveData<ApisResponse<out String>>()
    val doPrinting: LiveData<ApisResponse<out String>>
        get() = _doPrinting


    private val app=application

    private val repo=PrintRepository()


    fun isPrintConnected(){
        viewModelScope.launch {
            repo.isPrintSelected().collectLatest {
                _isPrinterConnected.postValue(it)
            }
        }
    }



    fun doPrint(response:PrintReceiptInfo){
        viewModelScope.launch {
            repo.doPrint(response).collectLatest {
                _doPrinting.postValue(it)
            }
        }
    }



    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }


}