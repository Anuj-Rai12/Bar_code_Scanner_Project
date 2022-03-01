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
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItem
import com.example.offiqlresturantapp.utils.ItemClickListerForListOfFood
import com.example.offiqlresturantapp.utils.Rs_Symbol
import com.example.offiqlresturantapp.utils.hide
import com.example.offiqlresturantapp.utils.show

class ConfirmOderFragmentAdaptor(
    private val itemClickListerForFoodSelected: (foodItem:FoodItem)->Unit,
    private val viewDeals: () -> Boolean
) :
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
        private var flagSelection: Boolean = false

        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("SetTextI18n")
        fun setData(
            foodItem: FoodItem,
            itemClickListerForFoodSelected: (foodItem:FoodItem)->Unit,
            viewDeals: () -> Boolean
        ) {
            binding.apply {
                foodItemName.apply {
                    setBg(foodItem.bg)
                    text = foodItem.foodName
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