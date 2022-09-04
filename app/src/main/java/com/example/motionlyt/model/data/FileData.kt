package com.example.motionlyt.model.data

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class FileData(
    val fileName: String? = null,
    val fileUrl: String? = null,
    val date: String? = null,
    val type: String? = null,
    val size: String? = null
) {
    companion object {
        val list = listOf(
            FileData("fILE1", "kldfglgkdfgkfdklfkd", "20/09/2002", "pdf", "23MB"),
            FileData("fILE2", "kldfglgkdfgkfdklfkd", "20/09/2002", "pdf", "23MB"),
            FileData("fILE3", "kldfglgkdfgkfdklfkd", "20/09/2002", "pdf", "23MB"),
            FileData("fILE4", "kldfglgkdfgkfdklfkd", "20/09/2002", "pdf", "23MB"),
            FileData("fILE5", "kldfglgkdfgkfdklfkd", "20/09/2002", "pdf", "23MB"),
            FileData("fILE6", "kldfglgkdfgkfdklfkd", "20/09/2002", "pdf", "22MB"),
            FileData("fILE7", "kldfglgkdfgkfdklfkd", "20/09/2002", "pdf", "210MB"),
        )
    }
}