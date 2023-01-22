package com.example.mpos.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.generic.GenericDataCls
import com.example.mpos.databinding.ItemBottomSheetBinding
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener


class GenericRecycleViewAdaptor<T>(private val list: List<T>) :
    RecyclerView.Adapter<GenericRecycleViewAdaptor<T>.GenericViewHolder>() {

    var onClickListener: OnBottomSheetClickListener? = null

    inner class GenericViewHolder(private val binding: ItemBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: T) {
            if (data is GenericDataCls) {
                binding.tvTitle.text = data.title
                binding.imgIcon.setImageResource(data.img)
            }

            binding.root.setOnClickListener {
                onClickListener?.onItemClicked(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val binding =
            ItemBottomSheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        val currItem = list[position]
        currItem?.let {
            holder.setData(it)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}