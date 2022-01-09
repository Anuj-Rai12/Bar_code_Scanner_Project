package com.example.offiqlresturantapp.ui.testingconnection.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.offiqlresturantapp.ui.testingconnection.model.api.ApKLoginPost
import com.example.offiqlresturantapp.ui.testingconnection.repo.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestingConnectionViewModel @Inject constructor(private val repository: ApiRepository) :
    ViewModel() {

    fun getApkLoginResponse(requestBody: ApKLoginPost,flag:Boolean) =
        repository.getApkLoginResponse(requestBody,flag).asLiveData()
}