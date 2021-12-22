package com.example.offiqlresturantapp.ui.searchfood.adaptor.offeradaptor


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.offiqlresturantapp.databinding.OfferItemLayoutBinding
import com.example.offiqlresturantapp.utils.hide
import com.example.offiqlresturantapp.utils.show

typealias OderOfferListCallBack = (String) -> Unit

class ListOfOfferAdaptor(
    private val oderOfferListCallBack: OderOfferListCallBack
) :
    RecyclerView.Adapter<ListOfOfferAdaptor.OderOfferListViewHolder>() {
    private var arrayList = ArrayList<String>()

    inner class OderOfferListViewHolder(private val binding: OfferItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var flag: Boolean = false

        fun setData(string: String, oderOfferListCallBack: OderOfferListCallBack) {
            binding.btnClickCheckBox.hide()
            binding.orderFoodItemDescTv.text = string
            binding.root.setOnClickListener {
                if (string == "No offer")
                    return@setOnClickListener
                flag = if (!flag) {
                    binding.btnClickCheckBox.show()
                    true
                } else {
                    binding.btnClickCheckBox.hide()
                    false
                }
                oderOfferListCallBack(string)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OderOfferListViewHolder {
        val binding =
            OfferItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OderOfferListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OderOfferListViewHolder, position: Int) {
        val curr = arrayList[position]
        holder.setData(curr, oderOfferListCallBack)
    }

    fun setMyData(list: List<String>) {
        arrayList.clear()
        arrayList.addAll(list)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}