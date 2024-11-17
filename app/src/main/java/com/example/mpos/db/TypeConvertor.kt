package com.example.mpos.db

import androidx.room.TypeConverter
import com.example.mpos.data.item_master_sync.json.UOMasterItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConvertor {

    @TypeConverter
    fun fromSubObjectList(value: List<UOMasterItem>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toSubObjectList(value: String): List<UOMasterItem> {
        val listType = object : TypeToken<List<UOMasterItem>>() {}.type
        return Gson().fromJson(value, listType)
    }
}