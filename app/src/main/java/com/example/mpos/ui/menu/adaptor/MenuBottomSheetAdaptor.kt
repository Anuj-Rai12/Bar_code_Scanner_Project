package com.example.mpos.ui.menu.adaptor

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.mnu.MnuData
import com.example.mpos.databinding.MnuItemLayoutBinding
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener

class MenuBottomSheetAdaptor :
    ListAdapter<MnuData<out Any>, MenuBottomSheetAdaptor.MnuItemViewHolder>(diffUtil) {

    var listener: OnBottomSheetClickListener? = null

    inner class MnuItemViewHolder(private val binding: MnuItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: MnuData<out Any>) {
            binding.menuTitle.text = data.title
            binding.root.setOnClickListener {
                listener?.onItemClicked(data)
                //itemClicked.invoke(data)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MnuData<out Any>>() {
            override fun areItemsTheSame(
                oldItem: MnuData<out Any>,
                newItem: MnuData<out Any>
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MnuData<out Any>,
                newItem: MnuData<out Any>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MnuItemViewHolder {
        val binding =
            MnuItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MnuItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MnuItemViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            holder.setData(it)
        }
    }

}