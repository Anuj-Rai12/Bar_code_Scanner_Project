package com.example.motionlyt.ui.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.motionlyt.ui.auth.repo.LoginRepository
import com.example.motionlyt.utils.Event

class ProfileViewModel(application: Application):AndroidViewModel(application) {

    private val repository = LoginRepository(application)
    private val app = application
    private val _event = MutableLiveData<Event<String>>()
    val event: LiveData<Event<String>>
        get() = _event




}