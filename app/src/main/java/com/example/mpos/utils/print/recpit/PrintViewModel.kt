package com.example.mpos.utils.print.recpit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.billing.printInvoice.json.PrintInvoice
import com.example.mpos.data.confirmOrder.response.json.PrintReceiptInfo
import com.example.mpos.utils.ApisResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.Serializable

class PrintViewModel(application: Application) : AndroidViewModel(application) {


    private val _isPrinterConnected = MutableLiveData<ApisResponse<out String>>()
    val isPrinterConnected: LiveData<ApisResponse<out String>>
        get() = _isPrinterConnected

    private val _doPrinting = MutableLiveData<ApisResponse<out Serializable>>()
    val doPrinting: LiveData<ApisResponse<out Serializable>>
        get() = _doPrinting

    private val _doPrintInvoicePrinting = MutableLiveData<ApisResponse<out String>>()
    val doPrintInvoicePrinting: LiveData<ApisResponse<out String>>
        get() = _doPrintInvoicePrinting

    private val _doPrintPineInvoicePrinting = MutableLiveData<ApisResponse<out Serializable>>()
    val doPrintPineInvoicePrinting: LiveData<ApisResponse<out Serializable>>
        get() = _doPrintPineInvoicePrinting

    private val repo = PrintRepository()


    fun init() {
        _doPrinting.postValue(null)
        _doPrintInvoicePrinting.postValue(null)
        _isPrinterConnected.postValue(null)
    }

    fun isPrintConnected() {
        viewModelScope.launch {
            repo.isPrintSelected().collectLatest {
                _isPrinterConnected.postValue(it)
            }
        }
    }


    fun doPrint(response: PrintReceiptInfo, times: Int) {
        viewModelScope.launch {
            repo.doPrint(response, times).collectLatest {
                _doPrinting.postValue(it)
            }
        }
    }

    fun doPrintInvoice(response: PrintInvoice) {
        viewModelScope.launch {
            repo.doPrintInvoice(response).collectLatest {
                _doPrintInvoicePrinting.postValue(it)
            }
        }
    }


    fun doPineLabPrintInvoice(response: PrintInvoice) {
        viewModelScope.launch {
            repo.doPineLabPrintInvoice(response).collectLatest {
                _doPrintPineInvoicePrinting.postValue(it)
            }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }


}