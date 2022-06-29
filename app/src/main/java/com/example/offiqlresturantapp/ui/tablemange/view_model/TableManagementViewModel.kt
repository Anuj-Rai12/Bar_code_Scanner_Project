package com.example.offiqlresturantapp.ui.tablemange.view_model


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.db.RoomDataBaseInstance
import com.example.offiqlresturantapp.di.RetrofitInstance
import com.example.offiqlresturantapp.ui.tablemange.repo.TblRepositoryImpl
import com.example.offiqlresturantapp.utils.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TableManagementViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application

    private val userSoredData = UserSoredData(app)

    private val _tblInfo = MutableLiveData<ApisResponse<out Any?>>()
    val tblInfo: LiveData<ApisResponse<out Any?>>
        get() = _tblInfo

    private lateinit var repositoryImpl: TblRepositoryImpl

    init {
        viewModelScope.launch {
            val userSoredData = UserSoredData(application)
            val db = RoomDataBaseInstance.getInstance(application)
            userSoredData.readBase.collectLatest {
                val auth = AllStringConst.getAuthHeader(genToken("${it.userId}:${it.passId}"))
                val retrofit = RetrofitInstance.getInstance(auth = auth, baseUrl = it.baseUrl)
                repositoryImpl =
                    TblRepositoryImpl(
                        application = application,
                        retrofit = retrofit.getRetrofit(),
                        roomDataBaseInstance = db
                    )
            }

        }
    }


    private val _event = MutableLiveData<Events<String>>()
    val events: LiveData<Events<String>>
        get() = _event

    fun fetchTbl() {
        if (!app.isNetworkAvailable()) {
            _event.postValue(Events("No Internet Connection"))
        }
        if (!this::repositoryImpl.isInitialized) {
            _event.postValue(Events("Unknown Error"))
        }
        viewModelScope.launch {
            userSoredData.read.collectLatest {
                if (checkFieldValue(it.storeNo.toString())) {
                    _event.postValue(Events("Internal Error \nTry Login Again"))
                } else {
                    repositoryImpl.getTblInformation(it.storeNo!!).collectLatest { res ->
                        _tblInfo.postValue(res)
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