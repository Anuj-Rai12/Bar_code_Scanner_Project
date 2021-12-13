package com.example.mltesting.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseLandmark
import com.vmadalin.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream
import kotlin.math.atan2


const val CAMERA_INT = 11
const val TAG = "ANUJ"
const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"


typealias LumaListener = (luma: Double) -> Unit
typealias ImageListener = (imageInput: InputImage) -> Unit

fun getIntent(string: String): Intent {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = string
    return intent
}

fun Context.msg(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}


object Convertor {
    fun covertImages2ByteArray(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    fun covertByteArray2image(array: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(array, 0, array.size)
    }
}

fun Activity.checkCameraPermission() =
    EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)


fun getAngle(
    firstPoint: PoseLandmark,
    midPoint: PoseLandmark,
    lastPoint: PoseLandmark
): Double {
    var result = Math.toDegrees(
        atan2(
            lastPoint.position.y - midPoint.position.y.toDouble(),
            lastPoint.position.x - midPoint.position.x.toDouble()
        )
                - atan2(
            firstPoint.position.y - midPoint.position.y,
            firstPoint.position.x - midPoint.position.x
        )
    )
    result = Math.abs(result) // Angle should never be negative
    if (result > 180) {
        result = 360.0 - result // Always get the acute representation of the angle
    }
    return result
}


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


fun getCameraImage(it: ActivityResult): Bitmap? {
    return if (it.resultCode == Activity.RESULT_OK) {
        it.data?.extras?.get("data") as Bitmap?
    } else
        null
}