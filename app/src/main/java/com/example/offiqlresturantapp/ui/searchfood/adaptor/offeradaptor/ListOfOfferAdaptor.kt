package com.example.offiqlresturantapp.ui.searchfood.adaptor.offeradaptor


import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.OfferItemLayoutBinding
import com.example.offiqlresturantapp.ui.searchfood.model.OfferDesc
import com.example.offiqlresturantapp.utils.hide
import com.example.offiqlresturantapp.utils.show

typealias OderOfferListCallBack = (offDesc: OfferDesc) -> Unit

class ListOfOfferAdaptor(
    private val oderOfferListCallBack: OderOfferListCallBack
) :
    ListAdapter<OfferDesc, ListOfOfferAdaptor.OderOfferListViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<OfferDesc>() {
            override fun areItemsTheSame(oldItem: OfferDesc, newItem: OfferDesc) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: OfferDesc, newItem: OfferDesc) =
                oldItem == newItem
        }
    }

    inner class OderOfferListViewHolder(private val binding: OfferItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var flag: Boolean = false

        @RequiresApi(Build.VERSION_CODES.M)
        fun setData(offer: OfferDesc, oderOfferListCallBack: OderOfferListCallBack) {
            binding.orderFoodItemDescTv.text = offer.title
            if (offer.title == "No offer")
                binding.btnClickCheckBox.hide()
            binding.root.setOnClickListener {
                if (offer.title == "No offer")
                    return@setOnClickListener
                flag = if (!flag) {
                    binding.btnClickCheckBox.apply {
                        show()
                        changeViewColor(binding.btnClickCheckBox, R.color.dark_green_color)
                    }
                    offer.selected = true
                    true
                } else {
                    binding.btnClickCheckBox.hide()
                    changeViewColor(binding.btnClickCheckBox,R.color.white)
                    offer.selected = false
                    false
                }
                oderOfferListCallBack(offer)
            }

        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun changeViewColor(view: View, color: Int) {
            view.setBackgroundColor(
                view.context.resources.getColor(
                    color,
                    null
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OderOfferListViewHolder {
        val binding =
            OfferItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OderOfferListViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: OderOfferListViewHolder, position: Int) {
        val curr = getItem(position)
        curr?.let {
            holder.setData(it, oderOfferListCallBack)
        }
    }

}