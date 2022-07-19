package com.example.mpos.ui.menu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.mnu.MenuType
import com.example.mpos.data.mnu.MnuData
import com.example.mpos.data.mnu.request.MenuItemRequest
import com.example.mpos.data.mnu.request.MenuItemRequestBody
import com.example.mpos.data.mnu.response.json.ItemList
import com.example.mpos.data.mnu.response.json.MenuDataResponse
import com.example.mpos.data.mnu.response.json.SubMenu
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


    private val _foodItemMnu = MutableLiveData<List<MnuData<out Any>>>()
    val foodItemMnu: LiveData<List<MnuData<out Any>>>
        get() = _foodItemMnu


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


    fun getFoodSubMenuItem(mnuData: MenuDataResponse?, foodItem: String) {
        if (mnuData == null) {
            _event.postValue(Events("Something Went Wrong!!"))
            return
        }
        val arr = mutableListOf<MnuData<SubMenu>>()
        val food = mnuData.find {
            val title = it.description[0].uppercaseChar() + it.description.substring(1)
                .lowercase(Locale.getDefault())
            title == foodItem
        }
        if (food == null || food.subMenu.isEmpty()) {
            _foodItemMnu.postValue(arr)
        } else {
            viewModelScope.launch {
                val def = async(IO) {
                    food.subMenu.forEachIndexed { index, subMenu ->
                        arr.add(
                            MnuData(
                                index,
                                subMenu.parameter ?: "Unknown Food",
                                MenuType.SubMenu.name,
                                subMenu
                            )
                        )
                    }
                    arr
                }
                _foodItemMnu.postValue(def.await())
            }
        }
    }


    fun getFoodItem(subMenu: SubMenu) {
        val arr = mutableListOf<MnuData<ItemList>>()
        if (subMenu.itemList.isNullOrEmpty()) {
            _foodItemMnu.postValue(arr)
            return
        }

        viewModelScope.launch {
            val def = async(IO) {
                subMenu.itemList.forEachIndexed { index, itemList ->
                    arr.add(MnuData(index, itemList.description, MenuType.ItemList.name, itemList))
                }
                arr
            }
            _foodItemMnu.postValue(def.await())
        }

    }


}