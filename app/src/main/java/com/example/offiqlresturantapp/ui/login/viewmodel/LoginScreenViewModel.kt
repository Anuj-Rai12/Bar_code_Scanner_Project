package com.example.offiqlresturantapp.ui.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.offiqlresturantapp.data.login.model.api.ApKLoginPost
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.di.RetrofitInstance
import com.example.offiqlresturantapp.ui.login.repo.LoginRepositoryImpl
import com.example.offiqlresturantapp.use_case.LoginUseCase
import com.example.offiqlresturantapp.utils.AllStringConst
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.Events
import com.example.offiqlresturantapp.utils.genToken
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val loginUseCase: LoginUseCase
        get() = LoginUseCase()


    private lateinit var repository: LoginRepositoryImpl
    private var storeId: String = "PER002"

    init {
        viewModelScope.launch {
            val userSoredData = UserSoredData(application)
            userSoredData.readBase.collectLatest {
                storeId = it.storeId
                val auth = AllStringConst.getAuthHeader(genToken("${it.userId}:${it.passId}"))
                val retrofit = RetrofitInstance.getInstance(auth = auth, baseUrl = it.baseUrl)
                repository = LoginRepositoryImpl(retrofit.getRetrofit(), userSoredData)
            }
        }
    }

    private val _event = MutableLiveData<Events<String>>()
    val events: LiveData<Events<String>>
        get() = _event


    private val _apkLogin = MutableLiveData<ApisResponse<out Any?>>()
    val apk: LiveData<ApisResponse<out Any?>>
        get() = _apkLogin


    fun checkLoginTraditional(userID: String, password: String) {
        if (!this::repository.isInitialized) {
            _event.postValue(Events("Unknown Error"))
            return
        }

        viewModelScope.launch {
            loginUseCase.getLoginResponse(userID, password, storeId).collectLatest {
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