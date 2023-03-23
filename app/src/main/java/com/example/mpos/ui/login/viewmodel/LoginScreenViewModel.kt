package com.example.mpos.ui.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.login.model.api.ApKLoginPost
import com.example.mpos.data.logoutstaff.LogOutRequest
import com.example.mpos.data.logoutstaff.LogOutRequestBody
import com.example.mpos.dataStore.UserSoredData
import com.example.mpos.di.RetrofitInstance
import com.example.mpos.ui.login.repo.LoginRepositoryImpl
import com.example.mpos.use_case.LoginUseCase
import com.example.mpos.utils.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val loginUseCase: LoginUseCase
        get() = LoginUseCase()
    private val app = application

    private lateinit var repository: LoginRepositoryImpl
    var storeId: String = "PER002"

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


    private val _logOutStaff = MutableLiveData<ApisResponse<out String?>>()
    val logOutStaff: LiveData<ApisResponse<out String?>>
        get() = _logOutStaff


    fun checkLoginTraditional(userID: String, password: String) {
        if (!this::repository.isInitialized) {
            _event.postValue(Events("Unknown Error"))
            return
        }
        if (!app.isNetworkAvailable()) {
            _event.postValue(Events("No Internet Connection Found"))
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


    fun staffLogOut() {
        if (!this::repository.isInitialized) {
            _event.postValue(Events("Unknown Error"))
            return
        }
        if (!app.isNetworkAvailable()) {
            _event.postValue(Events("No Internet Connection Found"))
            return
        }
        val staffId = RestaurantSingletonCls.getInstance().getUserId()!!
        viewModelScope.launch {
            repository.getLogOutResponse(LogOutRequest(LogOutRequestBody(staffId))).collectLatest {
                _logOutStaff.postValue(it)
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}