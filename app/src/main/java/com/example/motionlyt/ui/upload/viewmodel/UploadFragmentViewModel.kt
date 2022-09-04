package com.example.motionlyt.ui.upload.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.motionlyt.ui.upload.repo.UploadFileRepository
import com.example.motionlyt.utils.Event
import com.example.motionlyt.utils.NoInternt
import com.example.motionlyt.utils.ResponseWrapper
import com.example.motionlyt.utils.isNetworkAvailable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

class UploadFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val _event = MutableLiveData<Event<String>>()
    val event: LiveData<Event<String>>
        get() = _event

    private val app = application

    private val _fileUpload = MutableLiveData<ResponseWrapper<out String?>>()
    val fileUpload: LiveData<ResponseWrapper<out String?>>
        get() = _fileUpload

    private val uploadFileRepository = UploadFileRepository(app)

    init {
        if (!application.isNetworkAvailable()) {
            _event.postValue(Event(NoInternt))
        }
    }

    fun setUploadNull()=_fileUpload.postValue(null)
    fun uploadFile(uri: String, file: File) {
        if (!app.isNetworkAvailable()) {
            _event.postValue(Event(NoInternt))
            return
        }

        viewModelScope.launch {
            uploadFileRepository.uploadVideoUri(file, uri).collectLatest {
                _fileUpload.postValue(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}