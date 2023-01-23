package com.example.mpos.payment.qr

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class CreateQr {

    fun createQr(txt: String): Bitmap? {
        val qr = QRGEncoder(txt, null, QRGContents.Type.TEXT, 150)
        qr.colorBlack = Color.BLACK
        qr.colorWhite = Color.WHITE
        return try {
            qr.getBitmap(0)
        } catch (e: Exception) {
            Log.i("CREATE_QR", "createQr: ${e.localizedMessage}")
            null
        }
    }

    fun bitInputStream(bitmap: Bitmap): ByteArrayInputStream {
        val byteArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
        val bitmapData: ByteArray = byteArray.toByteArray()
        return ByteArrayInputStream(bitmapData)
        //val path= MediaStore.Images.Media.insertImage(context.contentResolver, bitmap,"IMG_" + System.currentTimeMillis(), null)
        //return Uri.parse(path)
    }



}