package com.example.offiqlresturantapp.ui.tablemange.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.data.table_info.model.json.TableDetail
import com.example.offiqlresturantapp.data.table_info.model.json.TblStatus
import com.example.offiqlresturantapp.databinding.TableTypeItemLayoutBinding
import com.example.offiqlresturantapp.utils.hide
import com.example.offiqlresturantapp.utils.show

typealias ItemTableClick = (data: TableDetail) -> Unit

class TableManagementAdaptor(private val itemClicked: ItemTableClick) :
    ListAdapter<TableDetail, TableManagementAdaptor.TableItemViewHolder>(diffUtil) {
    inner class TableItemViewHolder(private val binding: TableTypeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(
            tableData: TableDetail,
            itemClickListerForTableData: ItemTableClick
        ) {
            binding.apply {
                tableNumberTxt.text =
                    tableNumberTxt.context.getString(R.string.tbl_title_id, tableData.tableNo)

                when (TblStatus.valueOf(tableData.status)) {
                    TblStatus.Free -> {
                        tblEmpty.show()
                        tblReserved.hide()
                        tblOccupiedLayout.hide()
                        tblEmpty.text = tblEmpty.context.getString(R.string.open_table)
                    }
                    TblStatus.Reserved -> {
                        tblReserved.show()
                        tblEmpty.hide()
                        tblOccupiedLayout.hide()
                        tblReserved.text = TblStatus.Reserved.name
                    }
                    TblStatus.Occupied -> {
                        tblEmpty.hide()
                        tblReserved.hide()
                        tblOccupiedLayout.show()
                        occupiedTblMany.text = tableData.guestNumber
                        occupiedTblTime.text = ">${tableData.guestNumber}h"
                    }
                }
                root.setOnClickListener {
                    itemClickListerForTableData(tableData)
                }

            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TableDetail>() {
            override fun areItemsTheSame(
                oldItem: TableDetail,
                newItem: TableDetail
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: TableDetail,
                newItem: TableDetail
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableItemViewHolder {
        val binding =
            TableTypeItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TableItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TableItemViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            holder.setData(it, itemClicked)
        }
    }

}