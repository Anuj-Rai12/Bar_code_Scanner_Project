package com.example.offiqlresturantapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.offiqlresturantapp.data.item_master_sync.json.ItemMaster
import com.example.offiqlresturantapp.data.table_info.model.json.TableDetail

@Database(
    entities = [ItemMaster::class, TableDetail::class],
    version = 2
)
abstract class RoomDataBaseInstance : RoomDatabase() {

    abstract fun itemDao(): ItemMasterDao
    abstract fun tblDao(): TableDetailDao

    companion object {
        const val DatabaseName = "MY_RESTRUNTS_Table"
    }
}