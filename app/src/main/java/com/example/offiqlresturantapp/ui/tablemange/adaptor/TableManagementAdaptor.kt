package com.example.offiqlresturantapp.ui.tablemange.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.offiqlresturantapp.databinding.TableTypeItemLayoutBinding
import com.example.offiqlresturantapp.ui.tablemange.model.TableData
import com.example.offiqlresturantapp.utils.ItemClickListerForTableData
import com.example.offiqlresturantapp.utils.hide
import com.example.offiqlresturantapp.utils.show

class TableManagementAdaptor(private val itemClickListerForTableData: ItemClickListerForTableData) :
    ListAdapter<TableData, TableManagementAdaptor.ViewHolderForTableData>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TableData>() {
            override fun areItemsTheSame(oldItem: TableData, newItem: TableData): Boolean {
                return oldItem.tbl == newItem.tbl
            }

            override fun areContentsTheSame(oldItem: TableData, newItem: TableData): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolderForTableData(private val binding: TableTypeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(
            tableData: TableData,
            itemClickListerForTableData: ItemClickListerForTableData
        ) {
            binding.apply {
                tableNumberTxt.text = tableData.tbl

                when {
                    tableData.isOpen -> {
                        tblEmpty.show()
                        tblReserved.hide()
                        tblOccupiedLayout.hide()
                        tblEmpty.text = tableData.msg
                    }
                    tableData.isBooked -> {
                        tblReserved.show()
                        tblEmpty.hide()
                        tblOccupiedLayout.hide()
                        tblReserved.text = tableData.msg
                    }
                    tableData.isOccupied -> {
                        tblEmpty.hide()
                        tblReserved.hide()
                        tblOccupiedLayout.show()
                        occupiedTblMany.text = tableData.totalPeople.toString()
                        occupiedTblTime.text = ">${tableData.totalTime}h"
                    }
                }
                root.setOnClickListener {
                    itemClickListerForTableData(tableData)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderForTableData {
        val binding =
            TableTypeItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderForTableData(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderForTableData, position: Int) {
        val currData = getItem(position)
        currData?.let {
            holder.setData(it, itemClickListerForTableData)
        }
    }

}