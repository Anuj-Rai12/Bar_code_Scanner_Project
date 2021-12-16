package com.example.offiqlresturantapp.ui.searchfood.adaptor

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.FoodItemLayoutBinding
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItem
import com.example.offiqlresturantapp.utils.ItemClickListerForListOfFood
import com.example.offiqlresturantapp.utils.Rs_Symbol
import com.example.offiqlresturantapp.utils.hide
import com.example.offiqlresturantapp.utils.show

class ListOfFoodItemToSearchAdaptor(private val itemClickListerForListOfFood: ItemClickListerForListOfFood) :
    ListAdapter<FoodItem, ListOfFoodItemToSearchAdaptor.ListOfFoodItemViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FoodItem>() {
            override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
                return oldItem.foodName == newItem.foodName
            }

            override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ListOfFoodItemViewHolder(private val binding: FoodItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var flag: Boolean = false

        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("SetTextI18n")
        fun setData(
            foodItem: FoodItem,
            itemClickListerForListOfFood: ItemClickListerForListOfFood
        ) {

            binding.foodItemTv.text = foodItem.foodName
            binding.foodPriceTv.text = "$Rs_Symbol ${foodItem.foodPrice}"
            binding.offerDetailTv.text = setOffer(foodOffer = foodItem.foodOffer)
            binding.orderFoodItemDescTv.text = setOffer(foodOffer = foodItem.offerDesc)
            binding.totalFoodItemQtyTv.text = foodItem.foodQTY.toString()
            binding.btnAddCount.setOnClickListener {
                foodItem.foodQTY++
                binding.totalFoodItemQtyTv.text = foodItem.foodQTY.toString()
            }
            binding.btnMinusCount.setOnClickListener {
                if (foodItem.foodQTY > 0) {
                    foodItem.foodQTY--
                    binding.totalFoodItemQtyTv.text = foodItem.foodQTY.toString()
                }
            }
            binding.btnWithOffer.setOnClickListener {
                foodItem.addWithOffer = false
                itemClickListerForListOfFood(foodItem)
            }
            binding.btnWithoutOffer.setOnClickListener {
                foodItem.addWithOffer = false
                itemClickListerForListOfFood(foodItem)
            }

            binding.root.setOnClickListener {
                flag = if (!flag) {
                    changeViewColor(view = it)
                    setVisible()
                    true
                } else {
                    changeViewColor(view = it, color = R.color.semi_white_color)
                    setInVisible()
                    false
                }
            }


        }

        private fun setOffer(foodOffer: String?): String {
            return if (foodOffer != null) {
                binding.offerTv.show()
                foodOffer
            } else {
                binding.offerTv.hide()
                "No offer"
            }
        }

        private fun setVisible() {
            binding.apply {
                linearLayoutForItemCount.show()
                borderBottomFood.show()
                orderFoodItemDescTv.show()
                btnWithOffer.show()
                btnWithoutOffer.show()
            }
        }

        private fun setInVisible() {
            binding.apply {
                linearLayoutForItemCount.hide()
                borderBottomFood.hide()
                orderFoodItemDescTv.hide()
                btnWithOffer.hide()
                btnWithoutOffer.hide()
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun changeViewColor(view: View, color: Int = R.color.light_blue_bg) {
            view.setBackgroundColor(
                view.context.resources.getColor(
                    color,
                    null
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfFoodItemViewHolder {
        val binding =
            FoodItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListOfFoodItemViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ListOfFoodItemViewHolder, position: Int) {
        val curr = getItem(position)
        curr?.let {
            holder.setData(it, itemClickListerForListOfFood)
        }
    }
}