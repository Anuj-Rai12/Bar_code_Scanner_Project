package com.example.mpos.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

fun readFromFile(context: Context, path: String): String? {
    return try {
        val file = context.assets.open(path)
        val bufferedReader = BufferedReader(InputStreamReader(file))
        val stringBuilder = StringBuilder()
        bufferedReader.useLines { line ->
            line.forEach {
                stringBuilder.append(it)
            }
        }
        stringBuilder.toString()
    } catch (e: Exception) {
        createLogStatement("READ_FROM_FILE", e.localizedMessage ?: "Unknown Error")
        null
    }
}