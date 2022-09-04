package com.example.motionlyt.ui.share.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.motionlyt.ui.share.SharedRepository
import com.example.motionlyt.utils.Event
import com.example.motionlyt.utils.NoInternt
import com.example.motionlyt.utils.ResponseWrapper
import com.example.motionlyt.utils.isNetworkAvailable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ShareViewModel(application: Application) : AndroidViewModel(application) {

    private val _event = MutableLiveData<Event<String>>()
    val event: LiveData<Event<String>>
        get() = _event

    private val app = application


    private val _fileUpload = MutableLiveData<ResponseWrapper<out Any>>()
    val fileUpload: LiveData<ResponseWrapper<out Any>>
        get() = _fileUpload

    init {
        if (!application.isNetworkAvailable()) {
            _event.postValue(Event(NoInternt))
        }
    }

    private val repo = SharedRepository(app)

    fun getFileUpload() {
        if (!app.isNetworkAvailable()) {
            _event.postValue(Event(NoInternt))
            return
        }

        viewModelScope.launch {
            repo.getAllUploadFiles().collectLatest {
                _fileUpload.postValue(it)
            }
        }
    }


}