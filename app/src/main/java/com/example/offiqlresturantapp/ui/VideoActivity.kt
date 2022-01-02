package com.example.offiqlresturantapp.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.offiqlresturantapp.*
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.ViedoActivityLayoutBinding
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY


@AndroidEntryPoint
class VideoActivity : AppCompatActivity(), ImageAnalysis.Analyzer {
    private lateinit var binding: ViedoActivityLayoutBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private var flagForRecording = false
    private var videoCapture: VideoCapture? = null
    private var imageCapture: ImageCapture? = null
    private var flagForArgs: String? = null
    private val timer = object : CountDownTimer(30000, 1000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            binding.timerTxt.show()
            binding.timerTxt.text = "${millisUntilFinished / 1000} sec"
        }

        @SuppressLint("RestrictedApi")
        override fun onFinish() {
            binding.captureButton.setBackgroundColor(Color.RED)
            videoCapture?.stopRecording()
            binding.timerTxt.hide()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.hide()
        this.changeStatusBarColor(R.color.black)
        binding = ViedoActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        flagForArgs = intent.getStringExtra("Type")
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                startCameraX(cameraProvider)
            } catch (e: ExecutionException) {
                Log.i(TAG, "onCreate: $e")
                e.printStackTrace()
            } catch (e: InterruptedException) {
                Log.i(TAG, "onCreate: $e")
                e.printStackTrace()
            }
        }, getExecutor())

        binding.captureButton.setOnClickListener {
            if (flagForArgs == VIDEO)
                it.setUpVideoRecording()
            else
                it.setUpCamera()
        }
    }

    private fun View.setUpCamera() {
        captureImage {
            if (it) {
                setBackgroundColor(Color.GREEN)
            } else {
                setBackgroundColor(Color.BLACK)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun View.setUpVideoRecording() {
        if (!flagForRecording) {
            flagForRecording = true
            timer.start()
            setBackgroundColor(Color.GREEN)
            recordVideo()
        } else {
            flagForRecording = false
            timer.cancel()
            setBackgroundColor(Color.RED)
            videoCapture?.stopRecording()
        }
    }


    private fun captureImage(listener: (Boolean) -> Unit) {
        imageCapture?.let { imageCapture ->

            val contentValues = createFile("image/jpeg")
            imageCapture.takePicture(
                ImageCapture.OutputFileOptions.Builder(
                    contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ).build(),
                getExecutor(),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Toast.makeText(
                            this@VideoActivity,
                            "Image is Capture successfully \n " +
                                    "${outputFileResults.savedUri}",
                            Toast.LENGTH_SHORT
                        ).show()
                        listener(true)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.i(TAG, "onError: ${exception.localizedMessage}")
                        listener(false)
                    }
                },
            )
        }
    }


    @SuppressLint("RestrictedApi")
    private fun recordVideo() {
        videoCapture?.let { videoCapture ->
            val contentValue = createFile("video/mp4")
            try {

                videoCapture.startRecording(
                    VideoCapture.OutputFileOptions.Builder(
                        contentResolver,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        contentValue
                    ).build(), getExecutor(), object : VideoCapture.OnVideoSavedCallback {
                        override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                            Toast.makeText(
                                this@VideoActivity,
                                "Video is Saved \n At ${outputFileResults.savedUri}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onError(
                            videoCaptureError: Int,
                            message: String,
                            cause: Throwable?
                        ) {
                            Log.i(TAG, "onError: Video Not Saved Because \n $message")
                            Log.i(
                                TAG,
                                "onError: Video Not Saved Because Caused  \n ${cause?.localizedMessage}"
                            )
                        }

                    })


            } catch (e: Exception) {
                Log.i(TAG, "recordVideo Error: $e")
            }


        }
    }


    @SuppressLint("RestrictedApi")
    private fun startCameraX(cameraProvider: ProcessCameraProvider?) {
        cameraProvider?.let { cameraPro ->
            cameraPro.unbindAll()
            val cameraSelectorPor =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            imageCapture =
                ImageCapture.Builder().setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()
            videoCapture = VideoCapture.Builder().setVideoFrameRate(30).build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(getExecutor(), this)

            cameraPro.bindToLifecycle(
                (this as LifecycleOwner),
                cameraSelectorPor,
                preview,
                videoCapture,
                imageCapture
            )

        }
    }

    private fun getExecutor(): Executor {
        return ContextCompat.getMainExecutor(this)
    }

    override fun analyze(image: ImageProxy) {
        Log.i(TAG, "analyze: Got Frame At : ${image.imageInfo.timestamp}")
        image.close()
    }


    private fun createFile(type: String): ContentValues {
        val timeStamp = System.currentTimeMillis()
        val contentValue = ContentValues()
        contentValue.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp)
        contentValue.put(MediaStore.MediaColumns.MIME_TYPE, type)
        return contentValue
    }

}