package com.example.motionlyt.ui.share

import android.content.Context
import com.example.motionlyt.datastore.NotesSharedPreference
import com.example.motionlyt.model.data.FileData
import com.example.motionlyt.model.userinfo.LogInUser
import com.example.motionlyt.model.userinfo.User
import com.example.motionlyt.utils.ResponseWrapper
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class SharedRepository(context: Context) {

    private val uni = "University"
    private val fireStore = FirebaseFirestore.getInstance()
    private val info = "Information"

    private val db = Firebase.database

    private val notesSharedPreference by lazy {
        NotesSharedPreference.getInstance(context)
    }

    fun getAllUploadFiles() = flow {
        emit(ResponseWrapper.Loading("Fetching Files..."))
        val data = try {
            val list = mutableListOf<FileData>()

            val ref = fireStore.collection(uni)
                .document(notesSharedPreference.getUniName()!!)
                .collection(notesSharedPreference.getReg()!!).get()
                .await()
            ref.documents.forEach {
                if (it.id != info) {
                    list.add(getUploadFile(it.id))
                }
            }
            ResponseWrapper.Success(list)
        } catch (e: Exception) {
            ResponseWrapper.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)

    private suspend fun getUploadFile(id: String): FileData {
        val ref = fireStore.collection(uni)
            .document(notesSharedPreference.getUniName()!!)
            .collection(notesSharedPreference.getReg()!!)
            .document(id).get()
            .await()

        return ref.toObject(FileData::class.java)!!
    }


    fun getUserFriends() = flow {
        emit(ResponseWrapper.Loading("Getting user Info"))
        val data = try {
            val university = notesSharedPreference.getUniName()
            val mutableList = mutableListOf<Pair<String, LogInUser>>()
            val userList = mutableListOf<User>()
            val data = db.reference.child("UserInfo").get().await()
            data.children.forEach {
                mutableList.add(Pair(it.key!!, it.getValue(LogInUser::class.java)!!))
            }
            val getCourse = mutableList.filter {
                it.second.uni == university
            }

            getCourse.forEach {
                val db = fireStore.collection(uni).document(it.second.uni!!).collection(it.first)
                    .document("Information").get()
                val res = db.await()
                if (res.exists()) {
                    val value = res.toObject(User::class.java)
                    userList.add(value!!)
                }
            }

            ResponseWrapper.Success(userList)
        } catch (e: Exception) {
            ResponseWrapper.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


}