package com.example.mpos.utils.print

import android.os.Environment
import android.util.Log
import java.io.File

class PrintUtils {
    companion object {
        private const val PROJECT_NAME = "MPOS_COST_ESTIMATION"
        fun getFileSaveLocation(): String {
            val folder = File(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                File.separator + PROJECT_NAME
            )


            if (!folder.exists()) {
                folder.mkdirs()
                Log.i("COMMON", "getFileSaveLocation: File is Created ${folder.exists()}")
            }

            Log.i("COMMON", "getFileSaveLocation: ${folder.path} is There ? ${folder.exists()}")

            return folder.path + File.separator
        }
    }
}