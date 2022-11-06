package com.example.mpos.ui.crosselling

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingItems
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingJsonResponse
import com.example.mpos.databinding.CrossSellingDialogBoxBinding
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.utils.checkFieldValue
import com.example.mpos.utils.hide
import com.example.mpos.utils.show
import com.example.mpos.utils.showSandbar

class CrossSellingDialog(private val activity: Activity) {
    private var alertDialog: AlertDialog? = null

    var itemClicked: OnBottomSheetClickListener? = null

    fun showCrossSellingDialog(response: CrossSellingJsonResponse) {
        val itemSelected = mutableListOf<CrossSellingItems>()
        var totalItem = 0.0
        val binding = CrossSellingDialogBoxBinding.inflate(activity.layoutInflater)

        alertDialog =
            AlertDialog.Builder(activity).setView(binding.root).setCancelable(false).show()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.itemTitle.text = response.description

        binding.itemItemSelected.text =
            "Selection Max ${response.maxSelection}: Min ${response.minSelection}"

        binding.totalCountOfSelectItem.text = "Total Size 0"
        binding.cancelBtn.setOnClickListener {
            alertDialog?.dismiss()
        }
        val crossAdaptor = CrossSellingAdaptor {
            if (itemSelected.contains(it)) {
                itemSelected.remove(it)
            } else {
                itemSelected.add(it)
                totalItem += if (checkFieldValue(it.price) || !it.price.isDigitsOnly()) 0.0
                else "%.4f".format(ListOfFoodItemToSearchAdaptor.setPrice(it.price)).toDouble()
            }
            binding.totalCountOfSelectItem.text = "Total Size ${itemSelected.size}"
        }
        binding.clearBtn.setOnClickListener {
            itemSelected.clear()
            binding.totalCountOfSelectItem.text = "Total Size ${itemSelected.size}"
            crossAdaptor.notifyDataSetChanged()
            crossAdaptor.isFlagReset = true
        }
        binding.submitBtn.setOnClickListener {
            if (itemSelected.size > response.maxSelection.toLong()) {
                binding.root.showSandbar("Cannot select more then ${response.maxSelection} items")
                return@setOnClickListener
            }
            if (itemSelected.size < response.minSelection.toLong()) {
                binding.root.showSandbar("Please select at-least ${response.minSelection} items")
                return@setOnClickListener
            }
            val cross = CrossSellingJsonResponse(
                childItemList = itemSelected,
                description = response.description,
                maxSelection = response.maxSelection,
                minSelection = response.minSelection,
                parentItem = response.parentItem
            )
            itemClicked?.onItemClicked(Pair(totalItem, cross))
            alertDialog?.dismiss()
        }
        binding.recycleViewItem.adapter = crossAdaptor
        crossAdaptor.submitList(response.childItemList)
        alertDialog?.show()
    }


    fun displayCrossSellingItem(response: CrossSellingJsonResponse) {
        val binding = CrossSellingDialogBoxBinding.inflate(activity.layoutInflater)

        alertDialog =
            AlertDialog.Builder(activity).setView(binding.root).setCancelable(false).show()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.itemTitle.text = response.description
        binding.itemItemSelected.text = "Total Item Selected ${response.childItemList.size}"

        val crossAdaptor = CrossSellingAdaptor {}
        binding.recycleViewItem.adapter = crossAdaptor
        crossAdaptor.submitList(response.childItemList)
        crossAdaptor.isFlagReset=true
        crossAdaptor.isEnable = false
        crossAdaptor.notifyDataSetChanged()


        binding.submitBtn.hide()
        binding.cancelBtn.hide()
        binding.clearBtn.text = "Cancel"

        binding.clearBtn.setOnClickListener {
            alertDialog?.dismiss()
        }


        alertDialog?.show()
    }

}