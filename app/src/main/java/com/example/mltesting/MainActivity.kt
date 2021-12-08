package com.example.mltesting

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.example.mltesting.databinding.ActivityMainBinding
import com.example.mltesting.utils.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

import java.io.*
import java.lang.Exception


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

    private val streamModeGesture by lazy {
        AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
            .build()
    }


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
        if (pose.allPoseLandmarks.isEmpty()) {
            binding.displayDataText.append("No Land-Mark Found")
            return
        }
        pose.allPoseLandmarks.forEachIndexed { index, poseLandmark ->
            binding.displayDataText.append("Point ${index + 1} -> ${poseLandmark.landmarkType}\n")
            if (pose.allPoseLandmarks.last() == poseLandmark) {
                createTextFile()
            }
        }
    }

    private fun createTextFile() {
        if (this.checkWriteStoragePermission()) {
            val fileFolder = "POSE_LAND_${System.currentTimeMillis()}"
            binding.myImage.drawable?.toBitmap()?.let {
                val title = binding.displayDataText.text.toString()
                // Getting Note On Drive And Save Image to Folder
                saveImageFromBitmapToLocation(it, fileFolder)
                generateNoteOnSD(fileFolder, title)
            }

        }
    }


    private fun saveImageFromBitmapToLocation(bitmap: Bitmap, fileFolder: String) {
        val fileName = "IMG_" + System.currentTimeMillis()

        val root = this.getFileDir(fileFolder)
        if (!root.exists()) {
            root.mkdirs()
        }

        val dest = File(root, "$fileName.jpeg")

        try {
            val out = FileOutputStream(dest)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            Log.i(TAG, "bitUrl: ${e.localizedMessage}")
        }

    }


    private fun generateNoteOnSD(sFileName: String, sBody: String) {
        try {
            val root = this.getFileDir(fileName = sFileName)
            if (!root.exists()) {
                root.mkdirs()
            }
            val gpxFileCreate = File(root, "$sFileName.txt")
            val writer = FileWriter(gpxFileCreate)
            writer.append(sBody)
            writer.flush()
            writer.close()
            this.msg("File is Saved at \n ${root.toUri()}", Toast.LENGTH_LONG)
        } catch (e: IOException) {
            Log.i(TAG, "generateNoteOnSD: ${e.localizedMessage}")
            this.msg("File Not Create Error")
        }
    }

    private fun callCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        requestCamera.launch(intent)
    }

    private fun getPermission() {
        if (!this.checkCameraPermission()) {
            requestPermission()
        }
        if (!this.checkReadStoragePermission()) {
            requestPermission(
                manifest = Manifest.permission.READ_EXTERNAL_STORAGE,
                code = Read_External_Storage,
                name = "Read Storage"
            )
        }

        if (!this.checkWriteStoragePermission()) {
            requestPermission(
                manifest = Manifest.permission.WRITE_EXTERNAL_STORAGE,
                code = Write_External_Storage,
                name = "Write Storage"
            )
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
        //callCamera()
    }


}