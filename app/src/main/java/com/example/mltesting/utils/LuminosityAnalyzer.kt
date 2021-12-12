package com.example.mltesting.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.mltesting.LumaListener
import com.example.mltesting.MainActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetector
import java.nio.ByteBuffer

class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            MainActivity.poseDetectorCom?.let { poseDetector ->
                getPoseByUsingFileUri(poseDetector, image)
            }
        }


        val buffer = imageProxy.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        val luma = pixels.average()

        listener(luma)


        imageProxy.close()
    }


    private fun getPoseByUsingFileUri(poseDetector: PoseDetector, image: InputImage) {
        poseDetector.process(image).addOnSuccessListener { pose ->
            setUpLandMarkPoints(pose)
        }.addOnFailureListener { exception ->
            Log.i(TAG, "getPose: ${exception.localizedMessage}")
        }
    }


    private fun setUpLandMarkPoints(pose: Pose) {
        //binding.displayDataText.text = "Pose LandMark-Type :-\n\n"
        Log.i(TAG, "setUpLandMarkPoints: Pose LandMark-Type :-\n\n")
        if (pose.allPoseLandmarks.isEmpty()) {
            Log.i(TAG, "setUpLandMarkPoints: Pose is Empty ")
            return
        }
        pose.allPoseLandmarks.forEachIndexed { index, poseLandmark ->
            //  binding.displayDataText.append("Point ${index + 1} -> ${poseLandmark.landmarkType}\n")
            Log.i(TAG, "setUpLandMarkPoints:  Point ${index + 1} -> ${poseLandmark.landmarkType}\n")
        }
        //this.msg("All LandMark type has Been displayed")
        Log.i(TAG, "\n\nsetUpLandMarkPoints: All LandMark Type hase Been Displayed")

    }


    /*private val ORIENTATIONS = SparseIntArray()

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 0)
        ORIENTATIONS.append(Surface.ROTATION_90, 90)
        ORIENTATIONS.append(Surface.ROTATION_180, 180)
        ORIENTATIONS.append(Surface.ROTATION_270, 270)
    }
*/
/*
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Throws(CameraAccessException::class)
    private fun getRotationCompensation(
        cameraId: String,
        activity: Activity,
        isFrontFacing: Boolean
    ): Int {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        val deviceRotation = activity.windowManager.defaultDisplay.rotation
        var rotationCompensation = ORIENTATIONS.get(deviceRotation) as Int

        // Get the device's sensor orientation.
        val cameraManager = activity.getSystemService(CAMERA_SERVICE) as CameraManager
        val sensorOrientation = cameraManager
            .getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        if (isFrontFacing) {
            rotationCompensation = (sensorOrientation + rotationCompensation) % 360
        } else { // back-facing
            rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360
        }
        return rotationCompensation
    }*/


}