package com.example.offiqlresturantapp.ui.camera.utils

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.offiqlresturantapp.utils.ImageListener
import com.example.offiqlresturantapp.utils.LumaListener
import com.google.mlkit.vision.common.InputImage
import java.nio.ByteBuffer

class LuminosityAnalyzer(
    private val listener: LumaListener,
    private val imageListener: ImageListener
) :
    ImageAnalysis.Analyzer {

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        val mediaImage = imageProxy.image
        mediaImage?.let { img ->
            val image = InputImage.fromMediaImage(img, imageProxy.imageInfo.rotationDegrees)
            imageListener(image)
        }

        val buffer = imageProxy.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        val luma = pixels.average()

        listener(luma)


        imageProxy.close()
    }


}