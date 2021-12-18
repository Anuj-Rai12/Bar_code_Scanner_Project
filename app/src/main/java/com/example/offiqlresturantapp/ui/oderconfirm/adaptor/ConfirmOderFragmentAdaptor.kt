package com.example.offiqlresturantapp.ui.oderconfirm.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.offiqlresturantapp.databinding.ListOfFoodItemSelectedBinding
import com.example.offiqlresturantapp.ui.oderconfirm.model.FoodItemSelected
import com.example.offiqlresturantapp.utils.ItemClickListerForFoodSelected
import com.example.offiqlresturantapp.utils.Rs_Symbol

class ConfirmOderFragmentAdaptor(private val itemClickListerForFoodSelected: ItemClickListerForFoodSelected) :
    ListAdapter<FoodItemSelected, ConfirmOderFragmentAdaptor.SelectedFoodItemViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FoodItemSelected>() {
            override fun areItemsTheSame(
                oldItem: FoodItemSelected,
                newItem: FoodItemSelected
            ): Boolean {
                return oldItem.foodName == newItem.foodName
            }

            override fun areContentsTheSame(
                oldItem: FoodItemSelected,
                newItem: FoodItemSelected
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    inner class SelectedFoodItemViewHolder(private val binding: ListOfFoodItemSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(
            foodItemSelected: FoodItemSelected,
            itemClickListerForFoodSelected: ItemClickListerForFoodSelected
        ) {
            binding.apply {
                foodItemName.apply {
                    setBg(foodItemSelected.bg)
                    text = foodItemSelected.foodName
                }
                qtyOfFood.apply {
                    setBg(foodItemSelected.bg)
                    text = foodItemSelected.foodQty.toString()
                }
                rateOfFood.apply {
                    setBg(foodItemSelected.bg)
                    text = "$Rs_Symbol ${foodItemSelected.foodRate}"
                }
                amtOfFoodTv.apply {
                    setBg(foodItemSelected.bg)
                    text = "$Rs_Symbol ${foodItemSelected.foodAmt}"
                }
                root.setOnClickListener {
                    itemClickListerForFoodSelected(foodItemSelected)
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