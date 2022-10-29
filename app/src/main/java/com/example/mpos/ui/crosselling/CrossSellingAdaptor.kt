package com.example.mpos.ui.crosselling

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingItems
import com.example.mpos.databinding.CrossSellingItemBinding
import com.example.mpos.utils.Rs_Symbol
import com.example.mpos.utils.hide
import com.example.mpos.utils.show

typealias itemClicked = (data: CrossSellingItems) -> Unit

class CrossSellingAdaptor(private val itemClicked: itemClicked) :
    ListAdapter<CrossSellingItems, CrossSellingAdaptor.CrossSellingItemViewHolder>(diffUtil) {

    var isFlagReset: Boolean = false

    inner class CrossSellingItemViewHolder(private val binding: CrossSellingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isClicked: Boolean = false
        val tick = binding.itemSuccessClick
        fun setData(data: CrossSellingItems, itemClicked: itemClicked) {
            binding.foodTitle.text = data.childTxt
            binding.qtyOfItemAndPrice.text = "Qty : 1 and Price : $Rs_Symbol ${data.price}"
            binding.itemSuccessClick.hide()
            binding.root.setOnClickListener {
                if (!isClicked) {
                    itemClicked.invoke(data)
                    binding.itemSuccessClick.show()
                } else {
                    binding.itemSuccessClick.hide()
                }
                isClicked = !isClicked
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CrossSellingItems>() {
            override fun areItemsTheSame(
                oldItem: CrossSellingItems,
                newItem: CrossSellingItems
            ) = oldItem.itemCode == newItem.itemCode

            override fun areContentsTheSame(
                oldItem: CrossSellingItems,
                newItem: CrossSellingItems
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
            if (isFlagReset){
                holder.tick.hide()
            }
                holder.setData(it, itemClicked)
        }
    }

}