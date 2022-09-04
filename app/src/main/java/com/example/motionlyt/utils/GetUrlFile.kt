package com.example.motionlyt.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContract
import java.util.*
import kotlin.math.roundToInt

class GetUriFile : ActivityResultContract<InputData, OutPutData>() {
    override fun createIntent(context: Context, input: InputData?): Intent {
        return Intent(input?.intent)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): OutPutData {
        return OutPutData(requestCode = resultCode == Activity.RESULT_OK, uri = intent?.data)
    }
}

class InputData(
    val intent: Intent
)

class OutPutData(
    val requestCode: Boolean,
    val uri: Uri?
)


fun getMimeType(uri: Uri, context: Context): String? {
    return if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
        val cr: ContentResolver? = context.contentResolver
        cr?.getType(uri)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
            uri.toString()
        )
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            fileExtension.lowercase(Locale.getDefault())
        )
    }
}

fun getMbOrKbSize(bytesDownloadedSoFar: Long): Pair<String, Int> {
    var size = (bytesDownloadedSoFar / 1048576).toDouble().roundToInt()
    var str = "${size}MB"
    if (size <= 0) {
        size = (bytesDownloadedSoFar / 1024).toDouble().roundToInt()
        str = "${size}KB"
    }
    return Pair(str, size)
}