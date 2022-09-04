package com.example.motionlyt.ui.share.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.motionlyt.databinding.FriendsProfileLayoutBinding
import com.example.motionlyt.model.userinfo.User
import com.example.motionlyt.utils.hide
import com.example.motionlyt.utils.show


class FriendsAdaptor :
    ListAdapter<User, FriendsAdaptor.FriendsViewHolder>(diffUtil) {

    private val _userList = MutableLiveData<MutableList<User>>()
    val userList: LiveData<MutableList<User>>
        get() = _userList


    inner class FriendsViewHolder(private val binding: FriendsProfileLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var flag: Boolean = false
        fun makeData(data: User) {
            binding.root.setOnClickListener {
                if (!flag) {
                    binding.isCheckEd.show()
                    binding.isCheckEd.isChecked = true
                    val mutableList = mutableListOf<User>()
                    _userList.value?.let {mutableList.addAll(it)  }
                    mutableList.add(data)
                    _userList.postValue(mutableList)
                } else {
                    binding.isCheckEd.hide()
                    _userList.value?.let { dt ->
                        if (dt.contains(data)) {
                            dt.remove(data)
                        }
                        _userList.postValue(dt)
                    }
                }
                flag = !flag
            }

            binding.textView.text = data.name + "\n" + data.joindate

            binding.userProfileTxt.text = data.name?.first()?.uppercaseChar().toString()


        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(
                oldItem: User,
                newItem: User
            ) = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val binding =
            FriendsProfileLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendsViewHolder(binding)
    }


    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            holder.makeData(it)
        }
    }

}