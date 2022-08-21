package com.example.mpos.utils


import com.example.mpos.data.deals.json.AddOnMenu
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem

class DealsStoreInstance {
    private val listOfItem = mutableListOf<ItemMasterFoodItem>()
    private var isDealSelected: Boolean = false
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

    fun setResetButtonClick(flag: Boolean) {
        resetButtonClick = flag
    }





    fun isResetButtonClick() = resetButtonClick


    fun clear(): Boolean {
        listOfItem.clear()
        isDealSelected = false
        return listOfItem.isEmpty()
    }


    fun addDealsItem(deals: AddOnMenu): Boolean {
        isDealSelected = true
        val item = ItemMasterFoodItem(
            itemMaster = ItemMaster(
                0,
                "",
                "Food",
                itemCode = deals.menuCode,
                deals.description,
                deals.description,
                deals.price.toString(),
                deals.price.toString()
            ),
            foodQty = 1,
            foodAmt = deals.price,
            bg = listOfBg.last(),
            isDeal = true
        )
        listOfItem.add(item)
        return true
    }

    /*fun isItemEmpty(): Boolean {
        return listOfItem.isEmpty()
    }*/
    fun deleteItem(item: ItemMasterFoodItem): Boolean {
        return listOfItem.remove(item)
    }

    fun isDealsSelected() = isDealSelected

    fun getItem(): List<ItemMasterFoodItem> {
        return listOfItem.toList()
    }

}