package com.example.mpos.payment.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.feedback.feedbck.FeedBackRequest
import com.example.mpos.data.feedback.invoice.FinalInvoiceSendRequest
import com.example.mpos.dataStore.UserSoredData
import com.example.mpos.di.RetrofitInstance
import com.example.mpos.payment.repo.FeedBackRepository
import com.example.mpos.utils.AllStringConst
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.genToken
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FeedBackViewModel(application: Application) : AndroidViewModel(application) {

    private val _isFeedBackSend = MutableLiveData<ApisResponse<out Any>?>()
    val isFeedBackSend: LiveData<ApisResponse<out Any>?>
        get() = _isFeedBackSend

    private val userSoredData = UserSoredData(application)

    private lateinit var feedRepo: FeedBackRepository

    init {
        viewModelScope.launch {
            userSoredData.readBase.collectLatest {
                val auth = AllStringConst.getAuthHeader(genToken("${it.userId}:${it.passId}"))
                val retrofit = RetrofitInstance.getInstance(auth = auth, baseUrl = it.baseUrl)
                feedRepo = FeedBackRepository(
                    retrofit = retrofit.getRetrofit()
                )
            }
        }

    }


    fun <T> getFeedBack(data: T) {
        if (!this::feedRepo.isInitialized) {
            return
        }
        viewModelScope.launch {
            if (data is FinalInvoiceSendRequest) {
                feedRepo.getFeedBackInvoiceResponse(data).collectLatest {
                    _isFeedBackSend.postValue(it)
                }
            }
            if (data is FeedBackRequest) {
                feedRepo.getFeedBackResponse(data).collectLatest {
                    _isFeedBackSend.postValue(it)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}