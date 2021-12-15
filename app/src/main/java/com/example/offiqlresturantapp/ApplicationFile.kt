package com.example.offiqlresturantapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationFile : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}