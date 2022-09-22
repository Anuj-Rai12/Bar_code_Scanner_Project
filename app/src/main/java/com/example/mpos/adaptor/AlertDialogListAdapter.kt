package com.example.mpos.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.example.mpos.R
import com.example.mpos.databinding.OfferItemLayoutBinding
import com.example.mpos.utils.getColorInt

/*
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.R
import com.example.mpos.databinding.OfferItemLayoutBinding
import com.example.mpos.utils.getColorInt

typealias DialogOptionMenu = (data: Int) -> Unit

class DialogOptionMenuItem(private val itemClicked: DialogOptionMenu) :
    ListAdapter<String, DialogOptionMenuItem.DialogOptionViewHolder>(diffUtil) {
    inner class DialogOptionViewHolder(private val binding: OfferItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: String, itemClicked: DialogOptionMenu,position: Int) {
            binding.btnClickCheckBox.visibility = View.INVISIBLE
            binding.root.setBackgroundColor(binding.root.context.getColorInt(R.color.semi_white_color))
            binding.root.updateLayoutParams<ConstraintLayout.LayoutParams> {
                setMargins(5, 5, 5, 5)
            }
            binding.orderFoodItemDescTv.text = data
            binding.root.setOnClickListener {
                itemClicked.invoke(position)
            }
        }
    }



    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogOptionViewHolder {
        val binding =
            OfferItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DialogOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DialogOptionViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            holder.setData(it, itemClicked,position)
        }
    }

}*/


class AlertDialogListAdapter(private val context: Context) : BaseAdapter() {


    private val data: ArrayList<String> = arrayListOf()

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val binding = OfferItemLayoutBinding.inflate(LayoutInflater.from(context))
        binding.btnClickCheckBox.visibility = View.INVISIBLE
        binding.root.setBackgroundColor(binding.root.context.getColorInt(R.color.light_blue_bg))
        binding.orderFoodItemDescTv.textSize=20f
        binding.orderFoodItemDescTv.updateLayoutParams<ConstraintLayout.LayoutParams> {
            setMargins(15,0,8,0)
        }
        binding.orderFoodItemDescTv.text = data[position]
        /*val listItem = inflater.inflate(R.layout.offer_item_layout, p2, false)
        val textView = listItem.findViewById(R.id.text_view) as TextView
        textView.text = data[position]*/

        return binding.root
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    fun addItem(list: List<String>) {
        data.clear()
        data.addAll(list)
    }

    fun addItem(str: String) {
        data.add(str)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }
}