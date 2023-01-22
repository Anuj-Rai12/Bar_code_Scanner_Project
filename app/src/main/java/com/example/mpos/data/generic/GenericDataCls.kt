package com.example.mpos.data.generic

import com.example.mpos.R
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import java.util.*

data class GenericDataCls(
    val title: String,
    val img: Int,
    val type: String,
    val data: Any
) {
    companion object {

        fun getBookingLs(foodItem: ItemMasterFoodItem): List<GenericDataCls> {
            val ls = mutableListOf<GenericDataCls>()
            val crossSellingFlag =
                foodItem.itemMaster.crossSellingAllow.lowercase(Locale.getDefault())
                    .toBoolean()

            val decimal = foodItem.itemMaster.decimalAllowed.lowercase(Locale.getDefault())
                .toBoolean()
            val deal = foodItem.isDeal
            if (!deal && !crossSellingFlag) {
                //food name
                ls.add(
                    GenericDataCls(
                        "Add Instruction.",
                        img = R.drawable.ic_edit, Type.ADDINSTRUCTION.name,
                        foodItem
                    )
                )
            }
            if (!deal && !crossSellingFlag && decimal) {
                //qty
                ls.add(
                    GenericDataCls(
                        "Update Quantity.",
                        img = R.drawable.ic_edit, Type.UPDTQTY.name, foodItem
                    )
                )
            }
            if (decimal && !crossSellingFlag) {
                //amt
                ls.add(
                    GenericDataCls(
                        "Update Amount.",
                        img = R.drawable.ic_edit, Type.UPDTAMTM.name, foodItem
                    )
                )
            }
            if (crossSellingFlag) {
                //cross selling
                ls.add(
                    GenericDataCls(
                        "View Order Item",
                        img = R.drawable.ic_view, Type.VIEWORDER.name, foodItem
                    )
                )
            }
            return ls.toList()
        }

        enum class Type {
            ADDINSTRUCTION,
            UPDTQTY,
            UPDTAMTM,
            VIEWORDER
        }
    }
}