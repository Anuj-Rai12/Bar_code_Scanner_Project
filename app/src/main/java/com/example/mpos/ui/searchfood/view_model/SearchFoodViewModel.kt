package com.example.mpos.ui.searchfood.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.dataStore.UserSoredData
import com.example.mpos.db.RoomDataBaseInstance
import com.example.mpos.di.RetrofitInstance
import com.example.mpos.ui.searchfood.repo.SearchFoodRepositoryImpl
import com.example.mpos.utils.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SearchFoodViewModel constructor(
    application: Application
) : AndroidViewModel(application) {

    private lateinit var repository: SearchFoodRepositoryImpl
    private val app = application
    private val userSoredData = UserSoredData(application)

    private val _fdInfo = MutableLiveData<ApisResponse<out Any?>>()
    val fdInfo: LiveData<ApisResponse<out Any?>>
        get() = _fdInfo

    private val _event = MutableLiveData<Events<String>>()
    val events: LiveData<Events<String>>
        get() = _event


    private val _crossSellingResponse = MutableLiveData<ApisResponse<out Any?>>()
    val crossSellingResponse: LiveData<ApisResponse<out Any?>>
        get() = _crossSellingResponse


    init {
        if (!application.isNetworkAvailable()) {
            _event.postValue(Events("No Internet Connection"))
        }
        viewModelScope.launch {
            val db = RoomDataBaseInstance.getInstance(application)

            userSoredData.readBase.collectLatest {
                if (checkFieldValue(it.baseUrl) || checkFieldValue(it.storeId) || checkFieldValue(it.userId) ||
                    checkFieldValue(it.passId)
                ) {
                    return@collectLatest
                }
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


    //For Login Check In Response Type
    fun fetchResponseApi(storeID:String) {
        if (!this::repository.isInitialized) {
            _event.postValue(Events("Unknown Error"))
            return
        }
        viewModelScope.launch {
            repository.getItemMasterSync(storeID,"Table Reservation").collectLatest { res ->
                _fdInfo.postValue(res)
            }
        }
    }



    fun getCrossSellingItem(itemCode: String) {
        if (!this::repository.isInitialized) {
            _event.postValue(Events("Unknown Error"))
            return
        }
        if (!app.isNetworkAvailable()) {
            _event.postValue(Events("No Internet Connection found"))
            return
        }
        viewModelScope.launch {
            repository.getCrossSellingResponse(itemCode).collectLatest {
                _crossSellingResponse.postValue(it)
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
                if (res.data.isNullOrEmpty()) {
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