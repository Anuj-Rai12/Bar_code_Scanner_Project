package com.example.offiqlresturantapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.offiqlresturantapp.model.Envelope
import com.example.offiqlresturantapp.model.test.apkJanLogin.EnvelopeOption
import com.example.offiqlresturantapp.model.test.apklogin.EnvelopePostItem
import com.example.offiqlresturantapp.repo.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    fun getResult(post: EnvelopeOption)=apiRepository.getResponse(post).asLiveData()

    fun getMyApi()=apiRepository.getMyApiResponse().asLiveData()

    fun getItemResult()=apiRepository.getItemResponse().asLiveData()


    fun getMyApiForRss()=apiRepository.getMyRssApiResponse().asLiveData()
}