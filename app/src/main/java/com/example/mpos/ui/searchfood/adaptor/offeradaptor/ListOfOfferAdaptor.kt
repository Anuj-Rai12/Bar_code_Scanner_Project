package com.example.mpos.ui.searchfood.adaptor.offeradaptor


import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.R
import com.example.mpos.databinding.OfferItemLayoutBinding
import com.example.mpos.ui.searchfood.model.OfferDesc
import com.example.mpos.utils.hide
import com.example.mpos.utils.show

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
            if (offer.selected) {
                binding.btnClickCheckBox.backgroundTintList =
                    ColorStateList.valueOf(
                        binding.btnClickCheckBox.resources.getColor(
                            R.color.dark_green_color,
                            null
                        )
                    )
            }
            binding.root.setOnClickListener {
                return@setOnClickListener
                /*if (!offer.selected)
                    return@setOnClickListener
                if (offer.title == "No offer")
                    return@setOnClickListener
                flag = if (!flag) {
                    binding.btnClickCheckBox.apply {
                        show()
                        binding.btnClickCheckBox.backgroundTintList =
                            ColorStateList.valueOf(
                                binding.btnClickCheckBox.resources.getColor(
                                    R.color.dark_green_color,
                                    null
                                )
                            )
                    }
                    offer.selected = true
                    true
                } else {
                    binding.btnClickCheckBox.backgroundTintList =
                        ColorStateList.valueOf(
                            binding.btnClickCheckBox.resources.getColor(
                                R.color.white,
                                null
                            )
                        )
                    offer.selected = false
                    false
                }*/
                oderOfferListCallBack(offer)
            }

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