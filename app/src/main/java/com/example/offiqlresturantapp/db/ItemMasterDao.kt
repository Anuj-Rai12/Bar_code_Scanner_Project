package com.example.offiqlresturantapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.offiqlresturantapp.data.item_master_sync.json.ItemMaster
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemMasterDao {

    @Query("Select * from ItemMaster_tbl")
    fun getAllItem(): Flow<List<ItemMaster>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllItem(articles: List<ItemMaster>)


    @Query("Select *From ItemMaster_tbl where itemName Like:searchQuery or itemDescription Like:searchQuery or itemCategory Like :searchQuery")
    fun searchResult(searchQuery: String): Flow<List<ItemMaster>>


    @Query("delete from ItemMaster_tbl")
    suspend fun deleteAllData()

}