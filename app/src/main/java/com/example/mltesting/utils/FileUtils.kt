package com.example.mltesting.utils

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import androidx.activity.result.ActivityResult
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.vmadalin.easypermissions.EasyPermissions
import kotlin.math.atan2


const val CAMERA_INT = 11
const val TAG = "ANUJ"

object FileUtils {
    const val img_file = "IMAGE_FILE"
}

data class PoseOption(val pose: Pose) {
    val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
    val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
    val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
    val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
    val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
    val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
    val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
    val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
    val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
    val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
    val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
    val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)
    val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
    val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
    val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
    val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
    val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
    val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
    val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
    val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
    val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
    val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)
    val nose = pose.getPoseLandmark(PoseLandmark.NOSE)
    val leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)
    val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
    val leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER)
    val rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER)
    val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)
    val rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER)
    val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
    val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
    val leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)
    val rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)
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


fun getCameraImage(it: ActivityResult): Bitmap? {
    return if (it.resultCode == Activity.RESULT_OK) {
        it.data?.extras?.get("data") as Bitmap?
    } else
        null
}