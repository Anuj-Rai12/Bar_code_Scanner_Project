package com.fbts.mpos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fbts.mpos.data.item_master_sync.json.ItemMaster
import com.fbts.mpos.data.table_info.model.json.TableDetail

@Database(
    entities = [ItemMaster::class, TableDetail::class],
    version = 2,
    exportSchema = false
)
abstract class RoomDataBaseInstance : RoomDatabase() {

    abstract fun itemDao(): ItemMasterDao
    abstract fun tblDao(): TableDetailDao

    companion object {
        private const val DatabaseName = "MY_RESTRUNTS_Table"

        @Volatile
        private var INSTANCE: RoomDataBaseInstance? = null

        fun getInstance(context: Context): RoomDataBaseInstance {
            synchronized(this) {
                val instance = INSTANCE
                if (instance != null) {
                    return instance
                }

                synchronized(this) {
                    val oldInstance = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDataBaseInstance::class.java,
                        DatabaseName
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = oldInstance
                    return oldInstance
                }
            }
        }
    }
}