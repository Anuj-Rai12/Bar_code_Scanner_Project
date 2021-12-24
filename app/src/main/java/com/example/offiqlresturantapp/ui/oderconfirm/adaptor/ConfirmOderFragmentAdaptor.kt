package com.example.offiqlresturantapp.ui.oderconfirm.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.offiqlresturantapp.databinding.ListOfFoodItemSelectedBinding
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItem
import com.example.offiqlresturantapp.utils.ItemClickListerForListOfFood
import com.example.offiqlresturantapp.utils.Rs_Symbol

class ConfirmOderFragmentAdaptor(private val itemClickListerForFoodSelected: ItemClickListerForListOfFood) :
    ListAdapter<FoodItem, ConfirmOderFragmentAdaptor.SelectedFoodItemViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FoodItem>() {
            override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
                return oldItem.foodName == oldItem.foodName
            }

            override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
                return oldItem == newItem
            }
        }
    }


    inner class SelectedFoodItemViewHolder(private val binding: ListOfFoodItemSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(
            foodItem: FoodItem,
            itemClickListerForFoodSelected: ItemClickListerForListOfFood
        ) {
            binding.apply {
                foodItemName.apply {
                    setBg(foodItem.bg)
                    text = foodItem.foodName
                }
                qtyOfFood.apply {
                    setBg(foodItem.bg)
                    text = foodItem.foodQTY.toString()
                }
                rateOfFood.apply {
                    setBg(foodItem.bg)
                    text = "$Rs_Symbol ${foodItem.foodPrice}"
                }
                amtOfFoodTv.apply {
                    setBg(foodItem.bg)
                    text = "$Rs_Symbol ${foodItem.foodAmt}"
                }
                root.setOnClickListener {
                    itemClickListerForFoodSelected(foodItem)
                }
            }
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

    override fun onBindViewHolder(holder: SelectedFoodItemViewHolder, position: Int) {
        val curr = getItem(position)
        curr?.let {
            holder.setData(it, itemClickListerForFoodSelected)
        }
    }
}