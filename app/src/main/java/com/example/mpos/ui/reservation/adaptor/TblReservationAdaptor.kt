package com.example.mpos.ui.reservation.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.reservation.response.json.GetReservationResponseItem
import com.example.mpos.databinding.BookTblItemLayoutBinding
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener


class TblReservationAdaptor :
    ListAdapter<GetReservationResponseItem, TblReservationAdaptor.TableResViewHolder>(diffUtil) {

    var itemClickListener: OnBottomSheetClickListener? = null

    inner class TableResViewHolder(private val binding: BookTblItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(
            data: GetReservationResponseItem,
            position: Int
        ) {
            binding.nameOfCustomer.text = data.customerName
            binding.phoneNumber.text = data.customerMobile
            binding.timeNumber.text = data.reserveTime
            binding.serialNumberTxt.text = position.toString()
            binding.linearLayout.setOnClickListener {
                itemClickListener?.onItemClicked(data)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GetReservationResponseItem>() {
            override fun areItemsTheSame(
                oldItem: GetReservationResponseItem,
                newItem: GetReservationResponseItem
            ) = oldItem.customerMobile == newItem.customerMobile

            override fun areContentsTheSame(
                oldItem: GetReservationResponseItem,
                newItem: GetReservationResponseItem
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
            holder.setData(it, position + 1)
        }
    }

}