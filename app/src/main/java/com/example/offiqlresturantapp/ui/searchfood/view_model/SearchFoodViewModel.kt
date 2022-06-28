package com.example.offiqlresturantapp.ui.searchfood.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.db.RoomDataBaseInstance
import com.example.offiqlresturantapp.di.RetrofitInstance
import com.example.offiqlresturantapp.ui.searchfood.repo.SearchFoodRepositoryImpl
import com.example.offiqlresturantapp.utils.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SearchFoodViewModel constructor(
    application: Application
) : AndroidViewModel(application) {

    private lateinit var repository: SearchFoodRepositoryImpl

    private val userSoredData = UserSoredData(application)
    private val _fdInfo = MutableLiveData<ApisResponse<out Any?>>()
    val fdInfo: LiveData<ApisResponse<out Any?>>
        get() = _fdInfo

    private val _event = MutableLiveData<Events<String>>()
    val events: LiveData<Events<String>>
        get() = _event

    init {
        if (!application.isNetworkAvailable()) {
            _event.postValue(Events("No Internet Connection"))
        }
        viewModelScope.launch {
            val db = RoomDataBaseInstance.getInstance(application)
            userSoredData.readBase.collectLatest {
                val auth = AllStringConst.getAuthHeader(genToken("${it.userId}:${it.passId}"))
                val retrofit = RetrofitInstance.getInstance(auth = auth, baseUrl = it.baseUrl)
                repository = SearchFoodRepositoryImpl(
                    application = application,
                    retrofit = retrofit.getRetrofit(),
                    roomDataBaseInstance = db
                )
            }
        }
    }


    fun fetchResponseApi() {
        if (!this::repository.isInitialized) {
            _event.postValue(Events("Unknown Error"))
            return
        }
        viewModelScope.launch {
            userSoredData.read.collectLatest {
                if (checkFieldValue(it.storeNo.toString())) {
                    _event.postValue(Events("Internal Error \nTry Login Again"))
                } else {
                    repository.getItemMasterSync(it.storeNo!!).collectLatest { res ->
                        _fdInfo.postValue(res)
                    }
                }
            }
        }
    }


    fun getInitialData() {
        viewModelScope.launch {
            repository.getSearchFoodItem().collectLatest {
                _fdInfo.postValue(it)
            }
        }
    }


    fun searchQuery(src: String) {
        viewModelScope.launch {
            repository.getSearchFoodItem(src).collectLatest { res ->
                if (res.data?.isNullOrEmpty() == true) {
                    _fdInfo.postValue(ApisResponse.Success(null))
                } else {
                    _fdInfo.postValue(res)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}