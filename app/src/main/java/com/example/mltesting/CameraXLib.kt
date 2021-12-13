package com.example.mltesting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.mltesting.databinding.ActvityCamLayoutBinding
import com.example.mltesting.utils.FILENAME_FORMAT
import com.example.mltesting.utils.LuminosityAnalyzer
import com.example.mltesting.utils.TAG
import com.example.mltesting.utils.msg
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraXLib : AppCompatActivity() {
    private lateinit var binding: ActvityCamLayoutBinding

    private var imageCapture: ImageCapture? = null
    private var flagList: Boolean = false
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    companion object {
        var barCodeCom: Barcode? = null
    }

    private val option = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC,
            Barcode.FORMAT_CODE_93,
            Barcode.FORMAT_CODABAR,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_PDF417,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E,
        )
        .build()


    private val scanner by lazy {
        BarcodeScanning.getClient(option)
        //  BarcodeScanning.getClient()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActvityCamLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startCamera()
        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            })
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor, LuminosityAnalyzer({ luma ->
                            Log.d("TAG", "Average luminosity: $luma")
                        }, { imageInput->
                            scanner.process(imageInput).addOnSuccessListener { list ->
                                if (!list.isNullOrEmpty() && !flagList) {
                                    barCodeCom = list.last()
                                    flagList = true
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                    return@addOnSuccessListener
                                }

                            }.addOnFailureListener { e ->
                                Log.i(TAG, "startCamera: Error is $e")
                                flagList = false
                            }
                        })
                    )
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun getBarCode(barCodeList: List<Barcode>, success: (Barcode) -> Unit) {
        if (barCodeList.isEmpty())
            return
        val barcode = barCodeList.first()
        barcode.let { barCode ->
            when (barCode.valueType) {
                Barcode.TYPE_URL -> {
                    this.msg("Url is Found")
                    val title = barCode.url?.title
                    val url = barCode.url?.url
                    Log.i(TAG, "getBarCode: $title\n")
                    Log.i(TAG, "getBarCode: $url\n")
                    success(barCode)
                    return
                }
                Barcode.TYPE_WIFI -> {
                    this.msg("WIFI is Found....")
                    val ssid = barCode.wifi?.ssid
                    val password = barCode.wifi?.password
                    val type = barCode.wifi?.encryptionType
                    Log.i(TAG, "getBarCode: $type\n")
                    Log.i(TAG, "getBarCode: $ssid\n")
                    Log.i(TAG, "getBarCode: $password\n")
                    success(barCode)
                    return
                }
                else -> {
                    Log.i(TAG, "getBarCode  Success  : ${barCode.rawValue}\n\n")
                    success(barCode)
                    return
                }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }


}