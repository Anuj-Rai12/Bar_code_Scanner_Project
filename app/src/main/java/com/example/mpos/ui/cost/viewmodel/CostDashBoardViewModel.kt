package com.example.mpos.ui.cost.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mpos.data.costestimation.request.CostEstimation
import com.example.mpos.di.RetrofitInstance
import com.example.mpos.ui.cost.repo.CostDashBoardRepository
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.Events
import com.example.mpos.utils.isNetworkAvailable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CostDashBoardViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application
    private var repository: CostDashBoardRepository? = null


    private val _events = MutableLiveData<Events<String>>()
     val event: LiveData<Events<String>>
        get() = _events


    private val _costEstimationResponse = MutableLiveData<ApisResponse<out String?>>()
     val costEstimationResponse: LiveData<ApisResponse<out String?>>
        get() = _costEstimationResponse


    init {

        repository =
            CostDashBoardRepository(
                RetrofitInstance.getInstance(
                    "UseLess String..",
                    "UseLess BaseUrl"
                ).getRetrofit()
            )
    }


     fun getCostEstimation(request: CostEstimation) {
        if (!app.isNetworkAvailable()) {
            _events.postValue(Events("No Internet Connection Found!!"))
            return
        }
        viewModelScope.launch {
            repository?.getCostEstimationParams(request)?.collectLatest {
                _costEstimationResponse.postValue(it)
            } ?: _events.postValue(Events("Oops Repository is Not Set Up!!"))
        }

    }



    fun addError(msg:String){
        _events.postValue(Events(msg))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}