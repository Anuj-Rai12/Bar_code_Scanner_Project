package com.example.mpos.utils


class RestaurantSingletonCls {
    private var storeId: String? = null
    private var userId: String? = null
    private var restaurantName: String? = null

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


    fun setStoreId(store: String) {
        storeId = store
    }

    fun setUserID(usr: String) {
        this.userId = usr
    }

    fun setRestaurantName(res: String) {
        this.restaurantName = res
    }

    fun getRestaurantName() = restaurantName

}