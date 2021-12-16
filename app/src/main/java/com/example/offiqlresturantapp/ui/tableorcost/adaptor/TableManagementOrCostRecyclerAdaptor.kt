package com.example.offiqlresturantapp.ui.tableorcost.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.TableItemLayoutBinding
import com.example.offiqlresturantapp.ui.tableorcost.model.SelectionDataClass
import com.example.offiqlresturantapp.utils.ItemClickListerForTableOrCost

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
            /*if (selectionDataClass.image == R.drawable.costestimiation) {
                val parms=binding.imageBtnToChoose.layoutParams
                *//*val paddingDp = 25;
                val density = context.getResources().getDisplayMetrics().density
                int paddingPixel = (int)(paddingDp * density);
                view.setPadding(0,paddingPixel,0,0);*//*
            }*/
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