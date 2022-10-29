package com.example.mpos.ui.searchfood.adaptor

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.FoodItemLayoutBinding
import com.example.mpos.ui.searchfood.adaptor.offeradaptor.ListOfOfferAdaptor
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.ui.searchfood.model.OfferDesc
import com.example.mpos.utils.*
import java.util.*

class ListOfFoodItemToSearchAdaptor(
    private val itemClickListerForListOfFood: ItemClickListerForListOfFood,
    private val itemClickListerCrossSelling: ItemClickListerForListOfFood
) : ListAdapter<ItemMaster, ListOfFoodItemToSearchAdaptor.ListOfFoodItemViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ItemMaster>() {
            override fun areItemsTheSame(oldItem: ItemMaster, newItem: ItemMaster): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ItemMaster, newItem: ItemMaster): Boolean {
                return oldItem == newItem
            }
        }

        fun setPrice(salePrice: String): Double {
            val data = salePrice.split(',')
            val str = StringBuilder()
            data.forEach {
                str.append(it)
            }
            return str.toString().toDouble()
        }
    }

    inner class ListOfFoodItemViewHolder(private val binding: FoodItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var listOfOfferAdaptor: ListOfOfferAdaptor
        //private var listOfOfferString = mutableListOf<OfferDesc>()

        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("SetTextI18n")
        fun setData(
            foodItem: ItemMaster,
            itemClickListerForListOfFood: ItemClickListerForListOfFood,
            itemClickListerCrossSelling: ItemClickListerForListOfFood
        ) {

            binding.foodItemTv.text =
                if (!checkFieldValue(foodItem.itemName)) foodItem.itemName else foodItem.itemDescription

            binding.foodPriceTv.text = "$Rs_Symbol ${foodItem.salePrice}"
            binding.offerDetailTv.text = setOffer()//foodItem.foodOffer)
            binding.totalFoodItemQtyTv.text = 1.toString()
            setUpRecycleView()// For Now its Null
            /*if (foodItem.offerDesc != null)
                listOfOfferAdaptor.submitList(foodItem.offerDesc)
            else*/


            listOfOfferAdaptor.submitList(
                listOf(
                    OfferDesc(
                        title = "No offer", price = 0.0, selected = false
                    )
                )
            )

            binding.btnAddCount.setOnClickListener {
                foodItem.foodQty++
                binding.totalFoodItemQtyTv.text = foodItem.foodQty.toString()
            }

            binding.btnMinusCount.setOnClickListener {
                if (foodItem.foodQty > 1) {
                    foodItem.foodQty--
                    binding.totalFoodItemQtyTv.text = foodItem.foodQty.toString()
                }
            }

            binding.btnWithOffer.setOnClickListener {
                foodItem.foodQty = if (foodItem.foodQty > 0) foodItem.foodQty else 1.0
                val amt = (setPrice(foodItem.salePrice) * foodItem.foodQty)
                foodItem.foodAmt = "%.4f".format(amt).toDouble()
                itemClickListerForListOfFood(
                    ItemMasterFoodItem(
                        foodItem, foodAmt = foodItem.foodAmt, foodQty = foodItem.foodQty
                    )
                )
                setInVisible()
            }
            binding.btnWithoutOffer.setOnClickListener {
                //foodItem.addWithOffer = false
                //foodItem.offerDesc = null
                foodItem.foodQty = if (foodItem.foodQty > 0) foodItem.foodQty else 1.0
                val amt = (setPrice(foodItem.salePrice) * foodItem.foodQty)
                foodItem.foodAmt = "%.4f".format(amt).toDouble()
                itemClickListerForListOfFood(
                    ItemMasterFoodItem(
                        foodItem, foodAmt = foodItem.foodAmt, foodQty = foodItem.foodQty
                    )
                )
                //binding.root.changeViewColor(color = R.color.semi_white_color)
                setInVisible()
            }

            binding.root.setOnClickListener {
                //it.changeViewColor(R.color.light_blue_bg)
                setVisible(foodItem)
            }
            binding.crossSellingOffer.setOnClickListener {
                setInVisible()
                foodItem.foodQty = if (foodItem.foodQty > 0) foodItem.foodQty else 1.0
                val amt = (setPrice(foodItem.salePrice) * foodItem.foodQty)
                foodItem.foodAmt = "%.4f".format(amt).toDouble()
                itemClickListerCrossSelling(
                    ItemMasterFoodItem(
                        foodItem, foodAmt = foodItem.foodAmt, foodQty = foodItem.foodQty
                    )
                )
            }
            binding.btnCloseId.setOnClickListener {
                //binding.root.changeViewColor(color = R.color.semi_white_color)
                setInVisible()
            }

        }

        private fun setUpRecycleView() {
            //foodItem: FoodItem? = null
            binding.orderOfferListView.apply {
                setHasFixedSize(false)
                listOfOfferAdaptor = ListOfOfferAdaptor {
                    /*if (!listOfOfferString.contains(it) && it.selected) {
                        foodItem.foodAmt += it.price
                        listOfOfferString.add(it)
                    } else if (listOfOfferString.contains(it) && !it.selected) {
                        foodItem.foodAmt -= it.price
                        listOfOfferString.remove(it)
                    }
                    Log.i(TAG, "setData: $listOfOfferString")*/
                }
                adapter = listOfOfferAdaptor
            }
        }

        private fun setOffer(): String {
            //binding.offerTv.hide()
            return "No offer"
            //foodOffer: String?
            /*return if (foodOffer != null) {
                binding.offerTv.show()
                foodOffer
            } else {
                binding.offerTv.hide()
                "No offer"
            }*/
        }

        private fun setVisible(foodItem: ItemMaster) {
            binding.apply {
                linearLayoutForItemCount.show()
                borderBottomFood.show()
                btnCloseId.show()
                orderOfferListView.show()
                btnWithoutOffer.show()
                //Order-Offer
                offerTv.hide()
                offerDetailTv.show()
                //crossSelling btn
                val crossSelling=foodItem.crossSellingAllow.lowercase(Locale.getDefault()).toBoolean()
                if (crossSelling) {
                    crossSellingOffer.show()
                }else{//Remove this else block
                    crossSellingOffer.show()
                }
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
                //OdderOffer
                offerTv.show()
                offerDetailTv.hide()
                //crossSelling btn
                crossSellingOffer.hide()
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
            holder.setData(it, itemClickListerForListOfFood,itemClickListerCrossSelling)
        }
    }
}