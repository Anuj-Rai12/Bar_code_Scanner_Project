package com.example.mpos.ui.crosselling

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.item_master_sync.json.UOMasterItem
import com.example.mpos.databinding.CrossSellingItemBinding
import com.example.mpos.utils.hide
import com.example.mpos.utils.show

typealias TypeListItem = (data: UOMasterItem) -> Unit

class UOMDialogAdaptor(private val itemClicked: TypeListItem) :
    ListAdapter<UOMasterItem, UOMDialogAdaptor.UOMDialogViewHolderViewHolder>(diffUtil) {

        var isItemSelected=MutableLiveData<String>()


    inner class UOMDialogViewHolderViewHolder(private val binding: CrossSellingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tick = binding.itemSuccessClick
        fun setData(data: UOMasterItem, itemClicked: TypeListItem) {
            binding.foodTitle.text = data.itemUom
            binding.qtyOfItemAndPrice.setText("Amount ${data.itemSalePrice} and Qty ${data.uomqty}")
            binding.root.setOnClickListener {
                itemClicked.invoke(data)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UOMasterItem>() {
            override fun areItemsTheSame(
                oldItem: UOMasterItem,
                newItem: UOMasterItem
            ) = oldItem.itemUom == newItem.itemUom

            override fun areContentsTheSame(
                oldItem: UOMasterItem,
                newItem: UOMasterItem
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UOMDialogViewHolderViewHolder {
        val binding =
            CrossSellingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UOMDialogViewHolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UOMDialogViewHolderViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            if (isItemSelected.value.equals(it.itemUom)) {
                holder.tick.show()
            }else{
                holder.tick.hide()
            }
            holder.setData(it, itemClicked)
        }
    }

}