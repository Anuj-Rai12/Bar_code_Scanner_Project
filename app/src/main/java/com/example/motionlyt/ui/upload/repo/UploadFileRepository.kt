package com.example.motionlyt.ui.upload.repo

import android.content.Context
import androidx.core.net.toUri
import com.example.motionlyt.datastore.NotesSharedPreference
import com.example.motionlyt.model.data.FileData
import com.example.motionlyt.utils.ResponseWrapper
import com.example.motionlyt.utils.getDate
import com.example.motionlyt.utils.getMbOrKbSize
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.File

class UploadFileRepository(context: Context) {

    private val storage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private val storageReference: StorageReference by lazy {
        storage.getReferenceFromUrl("gs://notesapp-1f207.appspot.com")
    }

    private val uni = "University"

    private val fireStore = FirebaseFirestore.getInstance()

    private val notesSharedPreference by lazy {
        NotesSharedPreference.getInstance(context)
    }


    fun uploadVideoUri(file: File, uri: String) = flow {
        emit(ResponseWrapper.Loading("Uploading file.."))
        val data = try {
            val res = storageReference.child("${notesSharedPreference.getReg()}/${file.name}")
            res.putFile(uri.toUri()).await()
            val downloadUrl = res.downloadUrl.await()
            val meta = res.metadata.await()
            val videoType = FileData(
                fileName = meta.name,
                fileUrl = downloadUrl.toString(),
                date = getDate(),
                type = meta.contentType,
                size = getMbOrKbSize(meta.sizeBytes).first
            )
            fireStore.collection(uni)
                .document(notesSharedPreference.getUniName()!!)
                .collection(notesSharedPreference.getReg()!!)
                .document("${videoType.fileName}").set(videoType)
                .await()

            ResponseWrapper.Success("File Upload Successfully")
        } catch (e: Exception) {
            ResponseWrapper.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


}