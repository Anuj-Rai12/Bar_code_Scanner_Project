package com.example.offiqlresturantapp.dataStore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.offiqlresturantapp.ui.testingconnection.model.api.ApkBody
import com.example.offiqlresturantapp.utils.AllStringConst
import com.example.offiqlresturantapp.utils.TAG
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AllStringConst.PREFERENCES_USER)

@Singleton
class UserSoredData @Inject constructor(@ApplicationContext private val context: Context) {
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

    suspend fun updateInfo(userId: String, password: String, storeId: String) {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[allStoreString.PASS_NO] = password
            mutablePreferences[allStoreString.STORE_NUMBER] = storeId
            mutablePreferences[allStoreString.USER_ID] = userId
        }
    }

    private val allStoreString = object {
        val USER_ID = stringPreferencesKey("userId")
        val PASS_NO = stringPreferencesKey("password")
        val STORE_NUMBER = stringPreferencesKey("storeId")
    }

}