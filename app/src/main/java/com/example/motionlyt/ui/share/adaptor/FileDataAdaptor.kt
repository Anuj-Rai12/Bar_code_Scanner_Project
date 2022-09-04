package com.example.motionlyt.ui.share.adaptor

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.motionlyt.databinding.FileItemLayoutBinding
import com.example.motionlyt.model.data.FileData

typealias listner = (data: FileData) -> Unit

class FileDataAdaptor(private val itemClicked: listner) :
    ListAdapter<FileData, FileDataAdaptor.FileDataViewHolder>(diffUtil) {
    inner class FileDataViewHolder(private val binding: FileItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: FileData, itemClicked: listner) {
            binding.shareItBtn.setOnClickListener {
                itemClicked.invoke(data)
            }
            binding.fileSize.text = data.type + "\t" + data.size
            binding.dateUpload.text = data.date
            binding.fileName.text = data.fileName
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FileData>() {
            override fun areItemsTheSame(
                oldItem: FileData,
                newItem: FileData
            ) = oldItem.fileName == newItem.fileName

            override fun areContentsTheSame(
                oldItem: FileData,
                newItem: FileData
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileDataViewHolder {
        val binding =
            FileItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileDataViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            holder.setData(it, itemClicked)
        }
    }

}