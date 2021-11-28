package com.example.mltesting.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.vmadalin.easypermissions.EasyPermissions
import kotlin.math.atan2


const val CAMERA_INT = 11
const val TAG = "ANUJ"


fun getIntent(string: String): Intent {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = string
    return intent
}


data class PoseOption(val pose: Pose) {
    val leftShoulder: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
    val rightShoulder: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
    val leftElbow: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
    val rightElbow: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
    val leftWrist: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
    val rightWrist: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
    val leftHip: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
    val rightHip: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
    val leftKnee: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
    val rightKnee: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
    val leftAnkle: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
    val rightAnkle: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)
    val leftPinky: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
    val rightPinky: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
    val leftIndex: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
    val rightIndex: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
    val leftThumb: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
    val rightThumb: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
    val leftHeel: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
    val rightHeel: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
    val leftFootIndex: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
    val rightFootIndex: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)
    val nose: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.NOSE)
    val leftEyeInner: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)
    val leftEye: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
    val leftEyeOuter: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER)
    val rightEyeInner: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER)
    val rightEye: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)
    val rightEyeOuter: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER)
    val leftEar: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
    val rightEar: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
    val leftMouth: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)
    val rightMouth: PoseLandmark? = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)
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