package com.example.mpos.ui.tablemange.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.R
import com.example.mpos.data.table_info.model.json.TableDetail
import com.example.mpos.data.table_info.model.json.TblStatus
import com.example.mpos.databinding.TableTypeItemLayoutBinding
import com.example.mpos.utils.hide
import com.example.mpos.utils.show

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
                        if (tableData.billPrinted.equals("No",true)) {
                            occupiedTblPrint.hide()
                            occupiedTblMany.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f
                            )
                        }
                        occupiedTblMany.text = tableData.guestNumber
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