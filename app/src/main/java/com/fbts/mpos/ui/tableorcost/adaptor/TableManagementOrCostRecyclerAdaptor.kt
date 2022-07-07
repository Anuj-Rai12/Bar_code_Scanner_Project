package com.fbts.mpos.ui.tableorcost.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fbts.mpos.databinding.TableItemLayoutBinding
import com.fbts.mpos.ui.tableorcost.model.SelectionDataClass
import com.fbts.mpos.utils.ItemClickListerForTableOrCost

class TableManagementOrCostRecyclerAdaptor(private val itemSelectionForTableOrCost: ItemClickListerForTableOrCost) :
    ListAdapter<SelectionDataClass, TableManagementOrCostRecyclerAdaptor.ViewHolderForTableOrCost>(
        diffUtil
    ) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SelectionDataClass>() {
            override fun areItemsTheSame(
                oldItem: SelectionDataClass,
                newItem: SelectionDataClass
            ): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(
                oldItem: SelectionDataClass,
                newItem: SelectionDataClass
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


    inner class ViewHolderForTableOrCost(private val binding: TableItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(
            selectionDataClass: SelectionDataClass,
            itemSelectionForTableOrCost: ItemClickListerForTableOrCost
        ) {

            binding.btnTxt.text = selectionDataClass.title
            binding.imageBtnToChoose.setImageResource(selectionDataClass.image)
            binding.imageBtnToChoose.setOnClickListener {
                itemSelectionForTableOrCost(selectionDataClass)
            }
            binding.root.setOnClickListener {
                itemSelectionForTableOrCost(selectionDataClass)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderForTableOrCost {
        val binding =
            TableItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderForTableOrCost(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderForTableOrCost, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            holder.setData(it, itemSelectionForTableOrCost)
        }
    }

}