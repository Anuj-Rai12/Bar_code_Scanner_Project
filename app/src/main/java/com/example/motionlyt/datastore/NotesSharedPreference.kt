package com.example.motionlyt.datastore

import android.content.Context
import android.content.SharedPreferences

class NotesSharedPreference(private val context: Context) {

    private val uni = "University"
    private val reg = "RegistrationNum"

    companion object {
        private const val SharedLoginPreferences = "LOGIN_SCREEN_PREFER"
        private var INSTANCE: NotesSharedPreference? = null
        fun getInstance(context: Context): NotesSharedPreference {
            if (INSTANCE == null) {
                INSTANCE = NotesSharedPreference(context)
            }
            return INSTANCE!!
        }
    }

    private val loginPreference: SharedPreferences by lazy {
        context.getSharedPreferences(SharedLoginPreferences, Context.MODE_PRIVATE)
    }


    fun getUniName(): String? {
        return loginPreference.getString(uni, "")
    }


    fun getReg(): String? {
        return loginPreference.getString(reg, "")
    }


    //Set Reg
    fun setUniName(college: String) {
        val editor = loginPreference.edit()
        editor.putString(uni, college)
        editor.apply()
    }

    fun setReg(regNo: String) {
        val editor = loginPreference.edit()
        editor.putString(reg, regNo)
        editor.apply()
    }


    fun removeAll() {
        loginPreference.edit().clear().apply()
    }

}