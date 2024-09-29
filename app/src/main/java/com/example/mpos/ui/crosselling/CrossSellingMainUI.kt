package com.example.mpos.ui.crosselling

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.crosssellingApi.response.json.ChilditemList
import com.example.mpos.databinding.CrossSellingItemBinding
import com.example.mpos.utils.hide
import com.example.mpos.utils.show

typealias crossSellingResponse = (data: ChilditemList) -> Unit

class CrossSellingMainUI(private val itemClicked: crossSellingResponse) :
    ListAdapter<ChilditemList, CrossSellingMainUI.CrossSellingMainUIViewHolder>(diffUtil) {


    inner class CrossSellingMainUIViewHolder(private val binding: CrossSellingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: ChilditemList, itemClicked: crossSellingResponse) {
            binding.fooItem.show()
            binding.fooItem.text = data.itemDesc
            binding.foodTitle.hide()
            binding.qtyOfItemAndPrice.hide()
            binding.root.setOnClickListener {
                itemClicked.invoke(data)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChilditemList>() {
            override fun areItemsTheSame(
                oldItem: ChilditemList,
                newItem: ChilditemList
            ) = oldItem.itemCode == newItem.itemCode

            override fun areContentsTheSame(
                oldItem: ChilditemList,
                newItem: ChilditemList
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CrossSellingMainUIViewHolder {
        val binding =
            CrossSellingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CrossSellingMainUIViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CrossSellingMainUIViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {

            holder.setData(it, itemClicked)
        }
    }

}