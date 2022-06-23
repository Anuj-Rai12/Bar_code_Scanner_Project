package com.example.offiqlresturantapp.utils

import com.example.offiqlresturantapp.data.table_info.model.json.TableDetail

class RestaurantSingletonCls {
    private var storeId: String? = null
    private var userId: String? = null
    private var tbl: Pair<TableDetail, Long>? = null

    companion object {
        private var INSTANCE: RestaurantSingletonCls? = null
        fun getInstance(): RestaurantSingletonCls {
            if (INSTANCE == null) {
                INSTANCE = RestaurantSingletonCls()
            }
            return INSTANCE!!
        }
    }

    fun getStoreId() = storeId

    fun getUserId() = userId
    fun getTable() = tbl


    fun setStoreId(store: String) {
        storeId = store
    }

    fun setUserID(usr: String) {
        this.userId = usr
    }

    fun setTbl(tableDetail: TableDetail, rand: Long) {
        this.tbl = Pair(tableDetail, rand)
    }

    fun removeTblValue() {
        this.tbl = null
    }


}