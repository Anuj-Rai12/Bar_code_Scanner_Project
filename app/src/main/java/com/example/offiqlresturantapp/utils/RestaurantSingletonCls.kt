package com.example.offiqlresturantapp.utils

class RestaurantSingletonCls {
    private var storeId: String? = null
    private var userId: String? = null

    companion object {
        private var INSTANCE: RestaurantSingletonCls? = null
        fun getInstance(): RestaurantSingletonCls {
            if (INSTANCE == null) {
                INSTANCE = RestaurantSingletonCls()
            }
            return INSTANCE!!
        }
    }

    public fun getStoreId() = storeId

    public fun getUserId() = userId


    public fun setStoreId(store: String) {
        storeId = store
    }

    public fun setUserID(usr: String) {
        this.userId = usr
    }
}