package com.example.motionlyt.ui.auth.repo

import android.content.Context
import com.example.motionlyt.datastore.NotesSharedPreference
import com.example.motionlyt.model.userinfo.LogInUser
import com.example.motionlyt.model.userinfo.User
import com.example.motionlyt.utils.ResponseWrapper
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class LoginRepository(context: Context) {

    private val notesSharedPreference by lazy {
        NotesSharedPreference.getInstance(context)
    }

    private val db = Firebase.database
    private val parent = "UserInfo/"
    private val uni = "University"
    private val fireStore = FirebaseFirestore.getInstance()

    //Create User Info
    fun createUserInfo(user: User) = flow {
        emit(ResponseWrapper.Loading("Creating User"))
        val data = try {
            fireStore.collection(uni).document(user.uni!!).collection(user.reg!!)
                .document("Information").set(user).await()

            val ref = db.reference.child("$parent${user.reg}")
            ref.setValue(LogInUser(pass = user.password, uni = user.uni)).await()
            notesSharedPreference.setReg(user.reg)
            notesSharedPreference.setUniName(user.uni)
            ResponseWrapper.Success(null)

        } catch (e: Exception) {
            ResponseWrapper.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)

    //Retrieve user info
    fun getUserInfo(uni: String, reg: String) = flow {
        emit(ResponseWrapper.Loading("Getting User\nInformation.."))
        val data = try {
            val db = fireStore.collection(this@LoginRepository.uni).document(uni).collection(reg)
                .document("Information").get()
            val res = db.await()
            if (res.exists()) {
                val value = res.toObject(User::class.java)
                ResponseWrapper.Success(value)
            } else
                ResponseWrapper.Error("Cannot get user info", null)
        } catch (e: Exception) {
            ResponseWrapper.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


    fun checkUserIsValid(reg: String, password: String) = flow {
        emit(ResponseWrapper.Loading("Validating User"))
        val data = try {
            val res = db.reference.child("$parent$reg")
            val result = res.get().await()
            if (result.exists()) {
                (result.getValue<LogInUser>())?.let { user ->
                    if (password == user.pass) {
                        notesSharedPreference.setReg(reg)
                        notesSharedPreference.setUniName(user.uni!!)
                        ResponseWrapper.Success(null)
                    } else {
                        ResponseWrapper.Error(
                            "Invalid Password!!",
                            null
                        )
                    }
                } ?: ResponseWrapper.Error("Failed to get User Information", null)
            } else {
                ResponseWrapper.Error(
                    "Invalid Registration number!!", null
                )
            }
        } catch (e: Exception) {
            ResponseWrapper.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


    /*fun setRealtimeRegAndPass(reg: String, password: String) = flow {
        emit(ResponseWrapper.Loading("Creating User"))
        val data = try {
            val ref = db.reference.child("$parent$reg")
            ref.setValue(password).await()
            ResponseWrapper.Success(null)
        } catch (e: Exception) {
            ResponseWrapper.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)*/

}