package com.example.offiqlresturantapp.ui.searchfood.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.ui.searchfood.repo.SearchFoodRepositoryImpl
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
class SearchFoodViewModel @Inject constructor(
    private val repository: SearchFoodRepositoryImpl,
    private val userSoredData: UserSoredData,
    application: Application
) : ViewModel() {


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