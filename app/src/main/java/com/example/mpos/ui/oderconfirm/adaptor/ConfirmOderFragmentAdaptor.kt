package com.example.mpos.ui.oderconfirm.adaptor

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.R
import com.example.mpos.databinding.ListOfFoodItemSelectedBinding
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.utils.*

class ConfirmOderFragmentAdaptor(
    private val itemClickListerForFoodSelected: (foodItem: ItemMasterFoodItem) -> Unit,
    private val itemClickListerForUpdate: (foodItem: ItemMasterFoodItem) -> Unit,
    private val itemClickInstructionLinter: (foodItem: ItemMasterFoodItem) -> Unit,
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


        @SuppressLint("SetTextI18n")
        fun setData(
            foodItem: ItemMasterFoodItem,
            itemClickListerForFoodSelected: (foodItem: ItemMasterFoodItem) -> Unit,
            itemClickListerForUpdate: (foodItem: ItemMasterFoodItem) -> Unit,
            itemClickInstructionLinter: (foodItem: ItemMasterFoodItem) -> Unit
        ) {
            binding.apply {
                foodItemName.apply {
                    setBg(foodItem.bg)
                    text =
                        if (!checkFieldValue(foodItem.itemMaster.itemName)) foodItem.itemMaster.itemName
                        else foodItem.itemMaster.itemDescription
                }
                btnClickViewDetail.setOnClickListener {
                    if (foodItem.isDeal){
                        return@setOnClickListener
                    }
                    if (showCheckBox || foodItem.free_txt.isEmpty()) {
                        if (!flagSelection) {
                            flagSelection = true
                            getTintColor(btnClickViewDetail, R.color.dark_green_color)
                        } else {
                            flagSelection = false
                            getTintColor(btnClickViewDetail, R.color.light_grey_black_color)
                        }
                        itemClickListerForFoodSelected(foodItem)
                    }
                }

                if (!showCheckBox && foodItem.free_txt.isNotEmpty()) {
                    checkBoxView.show()
                    checkBoxView.setImageResource(R.drawable.ic_info)
                    getTintColor(checkBoxView, R.color.dark_green_color)
                }




                qtyOfFood.setOnClickListener {
                    if (foodItem.isDeal){
                        return@setOnClickListener
                    }
                    if (showQtyBox)
                        itemClickListerForUpdate(foodItem)
                }
                foodItemName.setOnClickListener {
                    if (foodItem.isDeal){
                        return@setOnClickListener
                    }
                    if (showQtyBox){
                        itemClickInstructionLinter(foodItem)
                    }
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


        fun getTintColor(it: AppCompatImageButton, col: Int) {
            val color = it.context.getColorInt(col)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                it.background.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            } else {
                it.setColorFilter(
                    color,
                    PorterDuff.Mode.MULTIPLY
                )
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

    @SuppressLint("NotifyDataSetChanged")
    fun setCheckBoxType(flag: Boolean) {
        notifyDataSetChanged()
        showCheckBox = flag
    }


    fun setQtyBoxType(flag: Boolean) {
        notifyDataSetChanged()
        showQtyBox = flag
    }


    override fun onBindViewHolder(holder: SelectedFoodItemViewHolder, position: Int) {
        val curr = getItem(position)
        curr?.let {
            if (showCheckBox) {
                holder.checkBoxView.setImageResource(R.drawable.ic_check_box)
                holder.getTintColor(holder.checkBoxView,R.color.light_grey_black_color)
                holder.checkBoxView.show()
            } else {
                holder.checkBoxView.hide()
            }
            holder.setData(it, itemClickListerForFoodSelected, itemClickListerForUpdate,itemClickInstructionLinter)
        }
    }
}