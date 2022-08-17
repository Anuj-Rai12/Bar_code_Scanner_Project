package com.example.mpos.ui.deals.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.data.deals.json.AddOnMenu
import com.example.mpos.data.deals.scan_and_find_deals.json.ScanAndFindDealsJsonItem
import com.example.mpos.databinding.FoodItemLayoutBinding
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.searchfood.adaptor.offeradaptor.ListOfOfferAdaptor
import com.example.mpos.ui.searchfood.model.OfferDesc
import com.example.mpos.utils.Rs_Symbol
import com.example.mpos.utils.hide
import com.example.mpos.utils.show

class DealsAdaptor<T>(private val list: List<T>) :
    RecyclerView.Adapter<DealsAdaptor<T>.DealsViewHolder>() {

    var onClickListener: OnBottomSheetClickListener? = null

    inner class DealsViewHolder(private val binding: FoodItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var listOfOfferAdaptor: ListOfOfferAdaptor
        fun setData(data: T) {
            binding.btnWithoutOffer.setOnClickListener {
                onClickListener?.onItemClicked(data)
            }
            setUpRecycleView()
            if (data is AddOnMenu) {
                binding.orderOfferListView.show()
                binding.borderBottomFood.show()
                binding.foodItemTv.text = data.description
                binding.offerTv.text = "on Offer"
                binding.foodPriceTv.text = "$Rs_Symbol ${data.price}"
                val orderList = mutableListOf<OfferDesc>()
                data.itemList.forEach {
                    orderList.add(
                        OfferDesc(
                            title = it.description,
                            price = data.price,
                            selected = true
                        )
                    )
                }
                listOfOfferAdaptor.submitList(orderList)
                binding.btnWithoutOffer.text = "View Deals items"
                binding.btnWithoutOffer.show()
            } else if (data is ScanAndFindDealsJsonItem) {
                binding.foodItemTv.text = data.itemName
                binding.offerTv.text = "on Offer"
                binding.offerDetailTv.text = "QTY ${data.qty}"
                binding.offerDetailTv.show()
                binding.foodPriceTv.text = "$Rs_Symbol ${data.salePrice}"
                listOfOfferAdaptor.submitList(listOf())
            }
        }

        private fun setUpRecycleView() {
            //foodItem: FoodItem? = null
            binding.orderOfferListView.apply {
                setHasFixedSize(false)
                listOfOfferAdaptor = ListOfOfferAdaptor {
                    /*if (!listOfOfferString.contains(it) && it.selected) {
                        foodItem.foodAmt += it.price
                        listOfOfferString.add(it)
                    } else if (listOfOfferString.contains(it) && !it.selected) {
                        foodItem.foodAmt -= it.price
                        listOfOfferString.remove(it)
                    }
                    Log.i(TAG, "setData: $listOfOfferString")*/
                }
                adapter = listOfOfferAdaptor
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealsViewHolder {
        val binding =
            FoodItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DealsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DealsViewHolder, position: Int) {
        val currItem = list[position]
        currItem?.let {
            holder.setData(it)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}