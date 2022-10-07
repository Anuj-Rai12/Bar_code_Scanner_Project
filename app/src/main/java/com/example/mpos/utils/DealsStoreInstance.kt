package com.example.mpos.utils


import com.example.mpos.data.deals.json.AddOnMenu
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem

class DealsStoreInstance {
    private var resetButtonClick = false

    companion object {
        private var instance: DealsStoreInstance? = null
        fun getInstance(): DealsStoreInstance {
            if (instance == null) {
                instance = DealsStoreInstance()
            }
            return instance!!
        }
    }


    fun isResetButtonClick() = resetButtonClick

    fun setIsResetButtonClick(flag: Boolean) {
        resetButtonClick = flag
    }


    fun addDealsItem(deals: AddOnMenu): ItemMasterFoodItem {
        return ItemMasterFoodItem(
            itemMaster = ItemMaster(
                0,
                "",
                "Food",
                itemCode = deals.menuCode,
                deals.description,
                deals.description,
                deals.price.toString(),
                deals.price.toString(),
                decimalAllowed = false.toString(),
                crossSellingAllow = false.toString()
            ),
            foodQty = 1.0,
            foodAmt = deals.price,
            bg = listOfBg.last(),
            isDeal = true
        )
    }


}