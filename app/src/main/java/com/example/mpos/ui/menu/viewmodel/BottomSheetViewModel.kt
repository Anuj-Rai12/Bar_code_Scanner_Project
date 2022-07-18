package com.example.mpos.ui.menu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.mnu.request.MenuItemRequest
import com.example.mpos.data.mnu.request.MenuItemRequestBody
import com.example.mpos.data.mnu.response.json.MenuDataResponse
import com.example.mpos.dataStore.UserSoredData
import com.example.mpos.di.RetrofitInstance
import com.example.mpos.ui.menu.repo.MenuRepository
import com.example.mpos.utils.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*

class BottomSheetViewModel(application: Application) : AndroidViewModel(application) {
    //private val app = application

    private lateinit var mnuRepository: MenuRepository

    private var storeID = ""

    private val userSoredData = UserSoredData(application)

    private val _event = MutableLiveData<Events<String>>()
    val event: LiveData<Events<String>>
        get() = _event


    private val _mnuItem = MutableLiveData<ApisResponse<out Serializable>>()
    val mnuItem: LiveData<ApisResponse<out Serializable>>
        get() = _mnuItem


    private val _mnuTab = MutableLiveData<List<String>>()
    val mnuTab: LiveData<List<String>>
        get() = _mnuTab

    init {
        if (application.isNetworkAvailable()) {
            viewModelScope.launch {
                userSoredData.readBase.collectLatest { testConnection ->
                    if (checkFieldValue(testConnection.baseUrl) || checkFieldValue(testConnection.passId)
                        || checkFieldValue(testConnection.storeId)
                    ) {
                        _event.postValue(Events("Oops Cannot SetUp Connection!!"))
                    } else {
                        val auth =
                            AllStringConst.getAuthHeader(genToken("${testConnection.userId}:${testConnection.passId}"))
                        val retrofit =
                            RetrofitInstance.getInstance(
                                auth = auth,
                                baseUrl = testConnection.baseUrl
                            )
                        storeID = testConnection.storeId
                        mnuRepository = MenuRepository(retrofit.getRetrofit())

                        fetchMenuDetail()

                    }
                }
            }
        } else {
            _event.postValue(Events("No Internet Connection Found!!"))
        }

    }

    private fun fetchMenuDetail() {
        if (!this::mnuRepository.isInitialized) {
            _event.postValue(Events("Unknown Error"))
            return
        }

        viewModelScope.launch {
            mnuRepository.getMenuData(MenuItemRequest(MenuItemRequestBody(storeID))).collectLatest {
                _mnuItem.postValue(it)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun setUi(mnuDataLink: MenuDataResponse?) {
        if (mnuDataLink == null) {
            _event.postValue(Events("Cannot Load Data!!"))
            return
        }
        viewModelScope.launch {
            val def = async(IO) {
                val arr = mutableListOf<String>()
                mnuDataLink.forEach { item ->
                    val title = item.description[0].uppercaseChar() + item.description.substring(1)
                        .lowercase(Locale.getDefault())

                    arr.add(title)
                }
                arr
            }
            _mnuTab.postValue(def.await())
        }
    }
}