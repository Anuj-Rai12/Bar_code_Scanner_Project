package com.example.offiqlresturantapp.utils

import com.google.gson.Gson

object Helper {
    /*fun <T> serializeToJson(classFile: T): String? {
        val gson = Gson()
        return gson.toJson(classFile)
    }*/

    // Deserialize to single object.
    inline fun <reified T> deserializeFromJson(jsonFile: String?): T? {
        val gson = Gson()
        return gson.fromJson(jsonFile, T::class.java)
    }
}