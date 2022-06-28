package com.example.offiqlresturantapp.ui.testingconnection.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.offiqlresturantapp.data.login.model.api.ApKLoginPost
import com.example.offiqlresturantapp.data.login.model.api.ApkBody
import com.example.offiqlresturantapp.data.testconnection.TestingConnection
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.di.RetrofitInstance
import com.example.offiqlresturantapp.ui.testingconnection.repo.ApiRepository
import com.example.offiqlresturantapp.utils.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TestingConnectionViewModel constructor(
    application: Application,
) : AndroidViewModel(application) {

    private val app = application
    private val _event = MutableLiveData<Events<String>>()
    val events: LiveData<Events<String>>
        get() = _event

    private val _apkLogin = MutableLiveData<ApisResponse<out Any?>>()
    val apk: LiveData<ApisResponse<out Any?>>
        get() = _apkLogin


    private val _testingConnection = MutableLiveData<ApisResponse<out String>>()
    val testingConnection: LiveData<ApisResponse<out String>>
        get() = _testingConnection

    private val userSoredData = UserSoredData(app)

    private lateinit var repository: ApiRepository

    var scannerUrl: String? = null


    init {
        viewModelScope.launch {
            userSoredData.readBase.collectLatest {
                if (checkFieldValue(it.baseUrl) || checkFieldValue(it.storeId) || checkFieldValue(it.userId) ||
                    checkFieldValue(it.passId)
                ) {
                    repository = ApiRepository(
                        retrofit = RetrofitInstance(
                            "dfkds",
                            "https://www.google.com"
                        ).getRetrofit(),
                        userSoredData = userSoredData
                    )
                    _apkLogin.postValue(ApisResponse.Error("1", null))
                } else {
                    val auth = AllStringConst.getAuthHeader(genToken("${it.userId}:${it.passId}"))
                    val retrofit = RetrofitInstance.getInstance(auth = auth, baseUrl = it.baseUrl)
                    repository = ApiRepository(
                        retrofit = retrofit.getRetrofit(),
                        userSoredData = userSoredData
                    )
                }
            }
        }
    }


    fun doLoginProcess() {
        if (!this::repository.isInitialized) {
            _event.postValue(Events("Unknown Error"))
            return
        }
        if (app.isNetworkAvailable()) {
            viewModelScope.launch {
                userSoredData.read.collectLatest {
                    if (checkFieldValue(it.storeNo.toString())
                        || checkFieldValue(it.password.toString())
                        || checkFieldValue(it.userID.toString())
                    ) {
                        _apkLogin.postValue(ApisResponse.Error(null, null))
                    } else {
                        RestaurantSingletonCls.getInstance().setUserID(it.userID ?: "")
                        RestaurantSingletonCls.getInstance().setStoreId(it.storeNo ?: "")
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


    fun testTheUrl(userId: String, passWord: String, storeID: String) {
        if (!this::repository.isInitialized) {
            _event.postValue(Events("Unknown Error"))
            return
        }
        scannerUrl?.let { baseUrl ->
            viewModelScope.launch {
                repository.addTestingUrl(
                    TestingConnection(
                        baseUrl = baseUrl,
                        userId = userId,
                        passId = passWord,
                        storeId = storeID
                    )
                ).collectLatest {
                    _testingConnection.postValue(it)
                }
            }
        } ?: run {
            _event.postValue(Events("Please Scan Url"))
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}