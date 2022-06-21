package com.example.offiqlresturantapp.ui.tablemange.view_model

import android.app.Application
import androidx.lifecycle.*
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.ui.tablemange.repo.TblRepositoryImpl
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
class TableManagementViewModel @Inject constructor(
    private val repository: TblRepositoryImpl,
    private val userSoredData: UserSoredData,
    private val application: Application
) : ViewModel() {

    private val _tblInfo = MutableLiveData<ApisResponse<out Any?>>()
    val tblInfo: LiveData<ApisResponse<out Any?>>
        get() = _tblInfo

    private val _event = MutableLiveData<Events<String>>()
    val events: LiveData<Events<String>>
        get() = _event

    fun fetchTbl(){
        if (!application.isNetworkAvailable()) {
            _event.postValue(Events("No Internet Connection"))
        }
        viewModelScope.launch {
            userSoredData.read.collectLatest {
                if (checkFieldValue(it.storeNo.toString())) {
                    _event.postValue(Events("Internal Error \nTry Login Again"))
                } else {
                    repository.getTblInformation(it.storeNo!!).collectLatest { res ->
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