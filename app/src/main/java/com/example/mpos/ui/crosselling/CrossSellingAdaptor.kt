package com.example.mpos.ui.crosselling

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.CrossSellingItemBinding
import com.example.mpos.utils.hide
import com.example.mpos.utils.show

typealias itemClicked = (data: ItemMaster) -> Unit

class CrossSellingAdaptor(private val itemClicked:itemClicked) : ListAdapter<ItemMaster, CrossSellingAdaptor.CrossSellingItemViewHolder>(diffUtil) {
    inner class CrossSellingItemViewHolder(private val binding: CrossSellingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isClicked:Boolean=false
        fun setData(data: ItemMaster,itemClicked:itemClicked) {
            binding.foodTitle.text=data.itemName
            binding.itemSuccessClick.hide()
            binding.root.setOnClickListener {
                if (!isClicked){
                    itemClicked.invoke(data)
                    binding.itemSuccessClick.show()
                }else{
                    binding.itemSuccessClick.hide()
                }
                isClicked=!isClicked
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
            )= oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  CrossSellingItemViewHolder{
        val binding=CrossSellingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CrossSellingItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CrossSellingItemViewHolder, position: Int) {
        val currItem=getItem(position)
        currItem?.let {
        holder.setData(it,itemClicked)
        }
    }

}