package com.example.motionlyt.ui.auth.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.motionlyt.ui.auth.repo.LoginRepository
import com.example.motionlyt.utils.Event
import com.example.motionlyt.utils.NoInternt
import com.example.motionlyt.utils.ResponseWrapper
import com.example.motionlyt.utils.isNetworkAvailable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LoginRepository()
    private val app = application
    private val _event = MutableLiveData<Event<String>>()
    val event: LiveData<Event<String>>
        get() = _event


    private val _userAcc = MutableLiveData<ResponseWrapper<out Any?>>()
    val userAcc: LiveData<ResponseWrapper<out Any?>>
        get() = _userAcc

    init {
        if (!application.isNetworkAvailable()) {
            _event.postValue(Event(NoInternt))
        }
    }

    fun setUserAccount(pass: String, reg: String) {
        if (!app.isNetworkAvailable()) {
            _event.postValue(Event(NoInternt))
            return
        }
        viewModelScope.launch {
            repository.setRealtimeRegAndPass(password = pass, reg = reg).collectLatest {
                _userAcc.postValue(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}

