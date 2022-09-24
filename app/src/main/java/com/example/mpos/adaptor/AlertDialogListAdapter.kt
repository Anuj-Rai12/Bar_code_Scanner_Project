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
import com.example.mpos.utils.hide
import com.example.mpos.utils.show


class AlertDialogListAdapter(private val context: Context) : BaseAdapter() {


    private val data: ArrayList<String> = arrayListOf()

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val binding = OfferItemLayoutBinding.inflate(LayoutInflater.from(context))
        binding.txtInfo.show()
        binding.btnClickCheckBox.hide()
        binding.orderFoodItemDescTv.hide()
        binding.txtInfo.show()
        binding.root.setBackgroundColor(binding.root.context.getColorInt(R.color.light_blue_bg))
        binding.txtInfo.text = data[position]
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