package com.example.mpos.ui.crosselling

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingItems
import com.example.mpos.databinding.CrossSellingItemBinding
import com.example.mpos.payment.unit.Utils
import com.example.mpos.utils.Rs_Symbol
import com.example.mpos.utils.hide
import com.example.mpos.utils.show

typealias itemClicked = (data: CrossSellingItems) -> Unit

class CrossSellingAdaptor(private val itemClicked: itemClicked) :
    ListAdapter<CrossSellingItems, CrossSellingAdaptor.CrossSellingItemViewHolder>(diffUtil) {

    var isFlagReset: Boolean = false
    var isEnable = true

    inner class CrossSellingItemViewHolder(private val binding: CrossSellingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tick = binding.itemSuccessClick
        val root = binding.root
        fun setData(data: CrossSellingItems, itemClicked: itemClicked) {
            Utils.createLogcat("TAG_CROSS_SELLING", "ITEM CROSS SELLING -> ${data}")
            binding.foodTitle.text = data.childTxt
            binding.qtyOfItemAndPrice.text = "Qty : ${data.qty} , Price : $Rs_Symbol ${data.price} and UOM : ${data.uom}"
            binding.root.setOnClickListener {
                if (!data.isClicked) {
                    binding.itemSuccessClick.show()
                } else {
                    binding.itemSuccessClick.hide()
                }
                itemClicked.invoke(data)
                data.isClicked = !data.isClicked
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CrossSellingItems>() {
            override fun areItemsTheSame(
                oldItem: CrossSellingItems, newItem: CrossSellingItems
            ) = oldItem.itemCode == newItem.itemCode

            override fun areContentsTheSame(
                oldItem: CrossSellingItems, newItem: CrossSellingItems
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrossSellingItemViewHolder {
        val binding =
            CrossSellingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CrossSellingItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CrossSellingItemViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {

            holder.root.isClickable = isEnable
            holder.root.isEnabled = isEnable

            if (isFlagReset) {
                holder.tick.hide()
                it.isClicked = false
            }

            if (!isEnable && isFlagReset) {
                holder.tick.show()
            }
            if(it.isClicked){
                holder.tick.show()
            }else{
                holder.tick.hide()
            }

            holder.setData(it, itemClicked)
        }
    }

}