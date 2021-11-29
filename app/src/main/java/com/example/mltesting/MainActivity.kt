package com.example.mltesting

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mltesting.databinding.ActivityMainBinding
import com.example.mltesting.utils.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityMainBinding
    private var bitmap: Bitmap? = null
    private var uri: Uri? = null
    private var poseDetector: PoseDetector? = null

    private val singleModeGesture by lazy {
        AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()
    }

    /*private val streamModeGesture by lazy {
        AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
            .build()
    }*/


    private val getImageFile = registerForActivityResult(GetUriFile()) {
        if (it.requestCode) {
            it.uri?.let { uri ->
                this.uri = uri
                binding.myImage.setImageURI(this.uri)
                getPoseByUsingFileUri()
            }
        }
    }

    private val requestCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it?.let {
                bitmap = getCameraImage(it)
                binding.myImage.setImageBitmap(bitmap)
                getPoseViaBitmap()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPermission()
        poseDetector = PoseDetection.getClient(singleModeGesture)

        binding.btnCamera.setOnClickListener {
            if (this.checkCameraPermission())
                callCamera()
        }

        binding.exploreBtn.setOnClickListener {
            getImageFile.launch(InputData(intent = getIntent("image/*")))
        }
    }


    private fun getPoseByUsingFileUri() {
        uri?.let {
            poseDetector?.process(InputImage.fromFilePath(this, it))
                ?.addOnSuccessListener { pose ->
                    setUpLandMarkPoints(pose)
                }?.addOnFailureListener { exception ->
                    Log.i(TAG, "getPose: ${exception.localizedMessage}")
                }
        }
    }

    private fun getPoseViaBitmap() {
        bitmap?.let {
            poseDetector?.process(InputImage.fromBitmap(it, 0))?.addOnSuccessListener { pose ->
                setUpLandMarkPoints(pose)
            }?.addOnFailureListener { exception ->
                Log.i(TAG, "getPose: ${exception.localizedMessage}")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpLandMarkPoints(pose: Pose) {
        binding.displayDataText.text = "Pose LandMark-Type :-\n\n"
        pose.allPoseLandmarks.forEachIndexed { index, poseLandmark ->
            binding.displayDataText.append("Point ${index + 1} -> ${poseLandmark.landmarkType}\n")
        }
        this.msg("All LandMark type has Been displayed")
    }

    private fun callCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        requestCamera.launch(intent)
    }

    private fun getPermission() {
        if (!this.checkCameraPermission()) {
            requestPermission()
        }
    }


    private fun requestPermission(
        manifest: String = Manifest.permission.CAMERA,
        code: Int = CAMERA_INT,
        name: String = "Camera"
    ) =
        EasyPermissions.requestPermissions(
            this,
            "Kindly Give us $name permission,otherwise application may not work Properly.",
            code,
            manifest,
        )

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        perms.forEach {
            if (EasyPermissions.permissionPermanentlyDenied(this, it)) {
                SettingsDialog.Builder(this).build().show()
            } else
                getPermission()
        }
    }


    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.i(TAG, "onPermissionsGranted: $requestCode is given $perms")
        callCamera()
    }


}