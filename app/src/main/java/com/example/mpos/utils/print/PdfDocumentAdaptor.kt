package com.example.mpos.utils.print

/*
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.util.Log
import com.example.mpos.utils.print.PrintBill.Companion.setCashAnalytics
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class PdfDocumentAdaptor constructor(private val path: String, private val context: Context) :
    PrintDocumentAdapter() {


    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: Bundle?
    ) {

        if (cancellationSignal?.isCanceled == true) {
            callback?.onLayoutCancelled()
        } else {
            val f = File(path)

            val pdfBuilder = PrintDocumentInfo
                .Builder(f.name)
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .build()

            callback?.onLayoutFinished(
                pdfBuilder,
                !newAttributes?.equals(oldAttributes)!!
            )

        }


    }

    override fun onWrite(
        pages: Array<out PageRange>?,
        destination: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        var inputOutStream: FileInputStream? = null
        var outputStream: FileOutputStream? = null
        try {
            val file = File(path)
            inputOutStream = FileInputStream(file)
            outputStream = FileOutputStream(destination?.fileDescriptor)

            val by = ByteArray(16384)
            var size = inputOutStream.read(by)
            while (size >= 0 && !cancellationSignal?.isCanceled!!) {
                outputStream.write(by, 0, size)
                size = inputOutStream.read(by)
            }
            if (cancellationSignal?.isCanceled!!) {
                callback?.onWriteCancelled()
            } else {
                val array = Array<PageRange>(1) { PageRange.ALL_PAGES }
                callback?.onWriteFinished(array)

            }
        } catch (e: Exception) {
            setCashAnalytics(e)
            Log.i(ContentValues.TAG, "onWrite: $e")
        } finally {
            try {
                inputOutStream?.close()
                outputStream?.close()
            } catch (e: Exception) {
                Log.i("WRITE ERROR", "onWrite: ${e.localizedMessage}")
                setCashAnalytics(e)
            }

        }


    }


}*/
