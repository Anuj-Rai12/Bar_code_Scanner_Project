package com.example.offiqlresturantapp.ui.testingconnection.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offiqlresturantapp.data.login.model.api.ApKLoginPost
import com.example.offiqlresturantapp.data.login.model.api.ApkBody
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.ui.testingconnection.repo.ApiRepository
import com.example.offiqlresturantapp.use_case.LoginUseCase
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.Events
import com.example.offiqlresturantapp.utils.checkFieldValue
import com.example.offiqlresturantapp.utils.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestingConnectionViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val loginUseCase: LoginUseCase,
    application: Application,
    private val userSoredData: UserSoredData
) : ViewModel() {


    private val _event = MutableLiveData<Events<String>>()
    val events: LiveData<Events<String>>
        get() = _event

    private val _apkLogin = MutableLiveData<ApisResponse<out Any?>>()
    val apk: LiveData<ApisResponse<out Any?>>
        get() = _apkLogin


    init {
        if (application.isNetworkAvailable()) {
            viewModelScope.launch {
                userSoredData.read.collectLatest {
                    if (checkFieldValue(it.storeNo.toString())
                        || checkFieldValue(it.password.toString())
                        || checkFieldValue(it.userID.toString())
                    ) {
                        _apkLogin.postValue(ApisResponse.Error(null, null))
                    } else {
                        repository.getApkLoginResponse(
                            ApKLoginPost(
                                ApkBody(
                                    storeNo = it.storeNo, userID =
                                    it.userID, password = it.password
                                )
                            ), flag = true
                        ).collectLatest { res -> _apkLogin.postValue(res) }
                    }
                }
            }
        } else {
            _event.postValue(Events("No Internet Connection"))
        }
    }


    fun checkLoginTraditional(
        userID: String,
        password: String,
        storeNo: String,
    ) {
        viewModelScope.launch {
            loginUseCase.getLoginResponse(userID, password, storeNo).collectLatest {
                if (it is ApisResponse.Loading) {
                    _event.postValue(Events(it.data.toString()))
                } else if (it is ApisResponse.Success) {
                    it.data?.let { item ->
                        val data = item as ApKLoginPost
                        repository.getApkLoginResponse(data, true).collectLatest { res ->
                            _apkLogin.postValue(res)
                        }
                    }
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}