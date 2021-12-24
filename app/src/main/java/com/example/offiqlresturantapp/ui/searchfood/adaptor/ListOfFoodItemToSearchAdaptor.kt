package com.example.offiqlresturantapp.ui.searchfood.adaptor

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.FoodItemLayoutBinding
import com.example.offiqlresturantapp.ui.searchfood.adaptor.offeradaptor.ListOfOfferAdaptor
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItem
import com.example.offiqlresturantapp.ui.searchfood.model.OfferDesc
import com.example.offiqlresturantapp.utils.*

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
        private lateinit var listOfOfferAdaptor: ListOfOfferAdaptor
        private var listOfOfferString = mutableListOf<OfferDesc>()

        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("SetTextI18n")
        fun setData(
            foodItem: FoodItem,
            itemClickListerForListOfFood: ItemClickListerForListOfFood
        ) {

            binding.foodItemTv.text = foodItem.foodName
            binding.foodPriceTv.text = "$Rs_Symbol ${foodItem.foodPrice}"
            binding.offerDetailTv.text = setOffer(foodOffer = foodItem.foodOffer)
            binding.totalFoodItemQtyTv.text = foodItem.foodQTY.toString()
            setUpRecycleView(foodItem)
            if (foodItem.offerDesc != null)
                listOfOfferAdaptor.submitList(foodItem.offerDesc)
            else
                listOfOfferAdaptor.submitList(
                    listOf(
                        OfferDesc(
                            title = "No offer",
                            price = 0,
                            selected = false
                        )
                    )
                )


            binding.btnAddCount.setOnClickListener {
                foodItem.foodQTY++
                binding.totalFoodItemQtyTv.text = foodItem.foodQTY.toString()
            }

            binding.btnMinusCount.setOnClickListener {
                if (foodItem.foodQTY > 1) {
                    foodItem.foodQTY--
                    binding.totalFoodItemQtyTv.text = foodItem.foodQTY.toString()
                }
            }

            binding.btnWithOffer.setOnClickListener {
                //foodItem.addWithOffer = true
                if (foodItem.offerDesc != null && !listOfOfferString.isNullOrEmpty()) {
                    foodItem.offerDesc = listOfOfferString
                } else if (foodItem.offerDesc != null && listOfOfferString.isNullOrEmpty()) {
                    foodItem.offerDesc?.forEach {
                        foodItem.foodAmt += it.price
                    }
                }
                foodItem.foodAmt *= foodItem.foodQTY
                itemClickListerForListOfFood(foodItem)
            }

            binding.btnWithoutOffer.setOnClickListener {
                //foodItem.addWithOffer = false
                foodItem.offerDesc = null
                foodItem.foodAmt = foodItem.foodPrice * foodItem.foodQTY
                itemClickListerForListOfFood(foodItem)
            }

            binding.root.setOnClickListener {
                it.changeViewColor(R.color.light_blue_bg)
                setVisible()
            }

            binding.btnCloseId.setOnClickListener {
                binding.root.changeViewColor(color = R.color.semi_white_color)
                setInVisible()
            }

        }

        private fun setUpRecycleView(foodItem: FoodItem) {
            binding.orderOfferListView.apply {
                setHasFixedSize(false)
                listOfOfferAdaptor = ListOfOfferAdaptor {
                    if (!listOfOfferString.contains(it) && it.selected) {
                        foodItem.foodAmt += it.price
                        listOfOfferString.add(it)
                    } else if (listOfOfferString.contains(it) && !it.selected) {
                        foodItem.foodAmt -= it.price
                        listOfOfferString.remove(it)
                    }
                    Log.i(TAG, "setData: $listOfOfferString")
                }
                adapter = listOfOfferAdaptor
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
                btnCloseId.show()
                orderOfferListView.show()
                btnWithOffer.show()
                btnWithoutOffer.show()
            }
        }

        private fun setInVisible() {
            binding.apply {
                linearLayoutForItemCount.hide()
                borderBottomFood.hide()
                btnCloseId.hide()
                btnWithOffer.hide()
                orderOfferListView.hide()
                btnWithoutOffer.hide()
            }
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