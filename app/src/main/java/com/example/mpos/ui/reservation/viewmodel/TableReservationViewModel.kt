package com.example.mpos.ui.reservation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.reservation.request.AddTableReservationRequest
import com.example.mpos.data.reservation.request.GetTableReservationRequest
import com.example.mpos.dataStore.UserSoredData
import com.example.mpos.di.RetrofitInstance
import com.example.mpos.ui.reservation.repo.TableReservationRepository
import com.example.mpos.utils.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.Serializable

class TableReservationViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application

    private lateinit var tableReservationRepository: TableReservationRepository


    private val userSoredData = UserSoredData(app)


    private val _event = MutableLiveData<Events<String>>()
    val events: LiveData<Events<String>>
        get() = _event


    private val _getAllReservationItem = MutableLiveData<ApisResponse<out Serializable>>()
    val getAllReservationItem: LiveData<ApisResponse<out Serializable>>
        get() = _getAllReservationItem


    private val _addReservationItem = MutableLiveData<ApisResponse<out Any>>()
    val addReservationItem: LiveData<ApisResponse<out Any>>
        get() = _addReservationItem

    private lateinit var staffID: String

    init {
        viewModelScope.launch {
            userSoredData.readBase.collectLatest {
                if (checkFieldValue(it.userId) || checkFieldValue(it.passId)) {
                    _event.postValue(Events("Cannot find Api Credentials!!"))
                }
                val auth = AllStringConst.getAuthHeader(genToken("${it.userId}:${it.passId}"))
                val retrofit = RetrofitInstance.getInstance(auth = auth, baseUrl = it.baseUrl)
                tableReservationRepository =
                    TableReservationRepository(retrofit = retrofit.getRetrofit())
                RestaurantSingletonCls.getInstance().getUserId().let { id ->
                    if (checkFieldValue(id.toString())) {
                        _event.postValue(Events("Cannot Find Staff_ID"))
                    }
                    staffID = id.toString()
                    Log.i("TESTING_ITEM", "StaffID: $staffID")
                }
            }
        }
    }


    fun getTableReservedInfo(request: GetTableReservationRequest) {
        if (!app.isNetworkAvailable()) {
            _event.postValue(Events("No Internet Connection Found!!"))
            return
        }
        if (!this::staffID.isInitialized || !this::tableReservationRepository.isInitialized) {
            _event.postValue(Events("Cannot SetUp Response Item"))
            return
        }

        viewModelScope.launch {
            tableReservationRepository.getReservationTable(request).collectLatest {
                _getAllReservationItem.postValue(it)
            }
        }
    }


    fun addReservation(reservationResponse: AddTableReservationRequest) {
        if (!app.isNetworkAvailable()) {
            _event.postValue(Events("No Internet Connection Found!!"))
            return
        }
        if (!this::staffID.isInitialized || !this::tableReservationRepository.isInitialized) {
            _event.postValue(Events("Cannot SetUp Response Item"))
            return
        }
        if (checkFieldValue(this.staffID)) {
            _event.postValue(Events("Invalid Staff_ID"))
            return
        }
        reservationResponse.body?.staffID = staffID
        viewModelScope.launch {
            tableReservationRepository.addReservationItem(reservationResponse).collectLatest {
                _addReservationItem.postValue(it)
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }


}