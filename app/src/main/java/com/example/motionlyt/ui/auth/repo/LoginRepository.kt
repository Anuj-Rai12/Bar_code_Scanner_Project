package com.example.motionlyt.ui.auth.repo

import com.example.motionlyt.utils.ResponseWrapper
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class LoginRepository {

    private val db = Firebase.database


    fun setRealtimeRegAndPass(reg: String, password: String) = flow {
        emit(ResponseWrapper.Loading("Creating User"))
        kotlinx.coroutines.delay(4000)
        val data = try {
            val ref = db.getReference(reg)
            ref.setValue(password).await()
            ResponseWrapper.Success(null)
        } catch (e: Exception) {
            ResponseWrapper.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)

}