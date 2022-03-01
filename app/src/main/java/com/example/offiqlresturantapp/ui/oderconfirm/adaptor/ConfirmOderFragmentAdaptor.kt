package com.example.offiqlresturantapp.ui.oderconfirm.adaptor

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.ListOfFoodItemSelectedBinding
import com.example.offiqlresturantapp.ui.searchfood.model.ItemMasterFoodItem
import com.example.offiqlresturantapp.utils.Rs_Symbol
import com.example.offiqlresturantapp.utils.checkFieldValue
import com.example.offiqlresturantapp.utils.hide
import com.example.offiqlresturantapp.utils.show

class ConfirmOderFragmentAdaptor(
    private val itemClickListerForFoodSelected: (foodItem: ItemMasterFoodItem) -> Unit,
    private val viewDeals: () -> Boolean
) :
    ListAdapter<ItemMasterFoodItem, ConfirmOderFragmentAdaptor.SelectedFoodItemViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ItemMasterFoodItem>() {
            override fun areItemsTheSame(
                oldItem: ItemMasterFoodItem,
                newItem: ItemMasterFoodItem
            ): Boolean {
                return oldItem.itemMaster.id == oldItem.itemMaster.id
            }

            override fun areContentsTheSame(
                oldItem: ItemMasterFoodItem,
                newItem: ItemMasterFoodItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    inner class SelectedFoodItemViewHolder(private val binding: ListOfFoodItemSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var flagSelection: Boolean = false

        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("SetTextI18n")
        fun setData(
            foodItem: ItemMasterFoodItem,
            itemClickListerForFoodSelected: (foodItem: ItemMasterFoodItem) -> Unit,
            viewDeals: () -> Boolean
        ) {
            binding.apply {
                foodItemName.apply {
                    setBg(foodItem.bg)
                    text =
                        if (!checkFieldValue(foodItem.itemMaster.itemName)) foodItem.itemMaster.itemName
                        else foodItem.itemMaster.itemDescription
                }
                if (viewDeals()) {
                    btnClickViewDetail.show()
                } else {
                    btnClickViewDetail.hide()
                }

                btnClickViewDetail.setOnClickListener {
                    it.backgroundTintList = if (!flagSelection) {
                        flagSelection = true
                        getTintColor(it, R.color.dark_green_color)
                    } else {
                        flagSelection = false
                        getTintColor(it, R.color.light_grey_black_color)
                    }
                    itemClickListerForFoodSelected(foodItem)
                }

                qtyOfFood.apply {
                    setBg(foodItem.bg)
                    text = foodItem.foodQty.toString()
                }
                rateOfFood.apply {
                    setBg(foodItem.bg)
                    text = "$Rs_Symbol ${foodItem.itemMaster.salePrice}"
                }
                amtOfFoodTv.apply {
                    setBg(foodItem.bg)
                    text = "$Rs_Symbol ${foodItem.foodAmt}"
                }
                /*root.setOnClickListener {
                    itemClickListerForFoodSelected(foodItem)
                }*/
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun getTintColor(it: View, color: Int): ColorStateList {
            return ColorStateList.valueOf(
                it.resources.getColor(
                    color,
                    null
                )
            )
        }

        private fun View.setBg(layout: Int) {
            this.setBackgroundResource(layout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedFoodItemViewHolder {
        val binding = ListOfFoodItemSelectedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SelectedFoodItemViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: SelectedFoodItemViewHolder, position: Int) {
        val curr = getItem(position)
        curr?.let {
            holder.setData(it, itemClickListerForFoodSelected, viewDeals)
        }
    }
}