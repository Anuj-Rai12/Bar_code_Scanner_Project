package com.example.mpos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpos.data.table_info.model.json.TableDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDetailDao {

    @Query("Select * from Table_for_status")
    fun getAllItem(): Flow<List<TableDetail>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllItem(articles: List<TableDetail>)

    @Query("delete from Table_for_status")
    suspend fun deleteAllData()

}