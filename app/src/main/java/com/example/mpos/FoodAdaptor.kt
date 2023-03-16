package com.example.mpos

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.FoodItemResultItemBinding

typealias listenerItem = (data: ItemMaster) -> Unit

class FoodAdaptor(private val itemClicked: listenerItem) :
    ListAdapter<ItemMaster, FoodAdaptor.FoodItemViewHolder>(diffUtil) {
    inner class FoodItemViewHolder(private val binding: FoodItemResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: ItemMaster, itemClicked: listenerItem) {
            binding.txtFood.text = data.itemName.ifEmpty {
                data.itemDescription
            }
            binding.root.setOnClickListener {
                itemClicked.invoke(data)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ItemMaster>() {
            override fun areItemsTheSame(
                oldItem: ItemMaster,
                newItem: ItemMaster
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ItemMaster,
                newItem: ItemMaster
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val binding =
            FoodItemResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            holder.setData(it, itemClicked)
        }
    }

}