package com.example.mpos.ui.reservation.adaptor

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.reverse.ReservationTbl
import com.example.mpos.databinding.BookTblItemLayoutBinding

typealias itemClickListener = (data: ReservationTbl) -> Unit

class TblReservationAdaptor(private val itemClicked: itemClickListener) :
    ListAdapter<ReservationTbl, TblReservationAdaptor.TableResViewHolder>(diffUtil) {
    inner class TableResViewHolder(private val binding: BookTblItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: ReservationTbl, itemClicked: itemClickListener) {
            binding.nameOfCustomer.text = data.customerName
            binding.phoneNumber.text = data.phoneNumber
            binding.timeNumber.text=data.time
            binding.serialNumberTxt.text = data.id.toString()
            binding.root.setOnClickListener {
                itemClicked.invoke(data)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ReservationTbl>() {
            override fun areItemsTheSame(
                oldItem: ReservationTbl,
                newItem: ReservationTbl
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ReservationTbl,
                newItem: ReservationTbl
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableResViewHolder {
        val binding =
            BookTblItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TableResViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TableResViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            holder.setData(it, itemClicked)
        }
    }

}