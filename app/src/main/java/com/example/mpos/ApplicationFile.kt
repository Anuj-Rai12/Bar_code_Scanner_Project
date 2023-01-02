package com.example.mpos

import android.app.Application
import android.content.Context


class ApplicationFile : Application() {
    companion object{
        lateinit var myContext:Context
    }
    override fun onCreate() {
        super.onCreate()
        myContext=this
    }
}