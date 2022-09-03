package com.example.motionlyt.ui.auth.repo

import com.example.motionlyt.model.userinfo.User
import com.example.motionlyt.utils.ResponseWrapper
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class LoginRepository {

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
            ref.setValue(user.password).await()

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
                (result.value as String?)?.let { pass ->
                    if (password == pass) {
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