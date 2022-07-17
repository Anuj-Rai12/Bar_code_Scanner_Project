package com.example.mpos.ui.oderconfirm.adaptor

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
import com.example.mpos.R
import com.example.mpos.databinding.ListOfFoodItemSelectedBinding
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.utils.Rs_Symbol
import com.example.mpos.utils.checkFieldValue
import com.example.mpos.utils.hide
import com.example.mpos.utils.show

class ConfirmOderFragmentAdaptor(
    private val itemClickListerForFoodSelected: (foodItem: ItemMasterFoodItem) -> Unit,
    private val itemClickListerForUpdate: (foodItem: ItemMasterFoodItem) -> Unit
) :
    ListAdapter<ItemMasterFoodItem, ConfirmOderFragmentAdaptor.SelectedFoodItemViewHolder>(diffUtil) {

    private var showCheckBox: Boolean = false
    private var showQtyBox: Boolean = true

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
        val checkBoxView = binding.btnClickViewDetail

        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("SetTextI18n")
        fun setData(
            foodItem: ItemMasterFoodItem,
            itemClickListerForFoodSelected: (foodItem: ItemMasterFoodItem) -> Unit,
            itemClickListerForUpdate: (foodItem: ItemMasterFoodItem) -> Unit
        ) {
            binding.apply {
                foodItemName.apply {
                    setBg(foodItem.bg)
                    text =
                        if (!checkFieldValue(foodItem.itemMaster.itemName)) foodItem.itemMaster.itemName
                        else foodItem.itemMaster.itemDescription
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

                qtyOfFood.setOnClickListener {
                    if (showQtyBox)
                    itemClickListerForUpdate(foodItem)
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

    @SuppressLint("NotifyDataSetChanged")
    fun setCheckBoxType(flag: Boolean) {
        notifyDataSetChanged()
        showCheckBox = flag
    }


    fun setQtyBoxType(flag: Boolean) {
        notifyDataSetChanged()
        showQtyBox = flag
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: SelectedFoodItemViewHolder, position: Int) {
        val curr = getItem(position)
        curr?.let {
            if (showCheckBox) {
                holder.checkBoxView.show()
            } else {
                holder.checkBoxView.hide()
            }
            holder.setData(it, itemClickListerForFoodSelected, itemClickListerForUpdate)
        }
    }
}