package com.example.offiqlresturantapp.dataStore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.offiqlresturantapp.data.login.model.api.ApkBody
import com.example.offiqlresturantapp.data.testconnection.TestingConnection
import com.example.offiqlresturantapp.utils.AllStringConst
import com.example.offiqlresturantapp.utils.TAG
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AllStringConst.PREFERENCES_USER)

class UserSoredData constructor(private val context: Context) {
    val read = context.dataStore.data.catch { e ->
        if (e is IOException) {
            Log.i(TAG, "READ_EXCEPTION: ${e.localizedMessage}")
            emit(emptyPreferences())
        } else {
            throw e
        }
    }.map { preferences ->
        val password = preferences[allStoreString.PASS_NO] ?: ""
        val storeId = preferences[allStoreString.STORE_NUMBER] ?: ""
        val userId = preferences[allStoreString.USER_ID] ?: ""
        ApkBody(
            storeNo = storeId,
            userID = userId,
            password = password
        )
    }

    suspend fun updateInfo(userId: String, password: String) {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[allStoreString.PASS_NO] = password
            mutablePreferences[allStoreString.USER_ID] = userId
        }
    }

    suspend fun updateBaseInfo(mainUrl: String, userId: String, password: String, storeId: String) {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[allStoreString.BASE_URL] = mainUrl
            mutablePreferences[allStoreString.MainUserID] = userId
            mutablePreferences[allStoreString.MainPasswordID] = password
            mutablePreferences[allStoreString.STORE_NUMBER] = storeId
        }
    }


    val readBase = context.dataStore.data.catch { e ->
        if (e is IOException) {
            Log.i(TAG, "READ_EXCEPTION: ${e.localizedMessage}")
            emit(emptyPreferences())
        } else {
            throw e
        }
    }.map { preferences ->
        val password = preferences[allStoreString.MainPasswordID] ?: ""
        val storeId = preferences[allStoreString.STORE_NUMBER] ?: ""
        val baseUrl = preferences[allStoreString.BASE_URL] ?: ""
        val userId = preferences[allStoreString.MainUserID] ?: ""
        val res = TestingConnection(
            baseUrl = baseUrl,
            storeId = storeId,
            userId = userId,
            passId = password
        )
        res
    }


    private val allStoreString = object {
        val USER_ID = stringPreferencesKey("userId")
        val PASS_NO = stringPreferencesKey("password")
        val STORE_NUMBER = stringPreferencesKey("storeId")
        val BASE_URL = stringPreferencesKey("BaseUrl")
        val MainUserID = stringPreferencesKey("MainUserID")
        val MainPasswordID = stringPreferencesKey("MainPasswordID")
    }

}