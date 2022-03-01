package com.example.offiqlresturantapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.offiqlresturantapp.data.item_master_sync.json.ItemMaster

@Database(
    entities = [ItemMaster::class],
    version = 1
)
abstract class RoomDataBaseInstance : RoomDatabase() {

    abstract fun itemDao(): ItemMasterDao

    companion object {
        const val DatabaseName = "MY_RESTRUNTS_Table"
    }
}