package com.example.mpos.ui.crosselling

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import com.example.mpos.data.crosssellingApi.response.json.ChilditemList
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingItems
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingJsonResponse
import com.example.mpos.data.item_master_sync.json.UOMasterItem
import com.example.mpos.databinding.CrossSellingDialogBoxBinding
import com.example.mpos.payment.unit.Utils
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.utils.checkFieldValue
import com.example.mpos.utils.hide
import com.example.mpos.utils.show
import com.example.mpos.utils.showSandbar

class CrossSellingDialog(private val activity: Activity) {
    private var alertDialog: AlertDialog? = null

    var itemClicked: OnBottomSheetClickListener? = null

    companion object {
        fun showCrossSellingItem(context: Context?, response: CrossSellingJsonResponse) {
            context?.let {
                val dialog = CrossSellingDialog(context as Activity)
                dialog.displayCrossSellingItem(response)
            }
        }
    }


    fun showOptionToSelectUOM(item: ItemMasterFoodItem, responses: List<UOMasterItem>,onIsDone:(Boolean)->Unit) {
        val binding = CrossSellingDialogBoxBinding.inflate(activity.layoutInflater)
        var adaptor: UOMDialogAdaptor? = null
        alertDialog =
            AlertDialog.Builder(activity).setView(binding.root).setCancelable(false).show()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var itemSelcted: UOMasterItem? = null
        binding.itemTitle.text = "Select UOM Option"
        adaptor=UOMDialogAdaptor {
            itemSelcted = it
            adaptor?.isItemSelected?.postValue(it.itemUom)
            adaptor?.submitList(responses)
            adaptor?.notifyDataSetChanged()
        }

        binding.submitBtn.setOnClickListener {
            if (itemSelcted != null) {
                item.itemMaster.uOM = itemSelcted?.itemUom.toString()
                onIsDone.invoke(true)
                alertDialog?.dismiss()
            } else {
                binding.root.showSandbar("Please choose any option")
            }
        }
        binding.clearBtn.hide()
        binding.cancelBtn.setOnClickListener {
            onIsDone.invoke(false)
            alertDialog?.dismiss()
        }
        binding.recycleViewItem.adapter = adaptor
        adaptor.submitList(responses)
        adaptor?.notifyDataSetChanged()
        alertDialog?.show()
    }


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    fun showCrossSellingDialog(response: CrossSellingJsonResponse, count: Int) {
        var selectionCount = count
        val mutableMainList = mutableListOf<ChilditemList>()
        val itemSelected = mutableListOf<CrossSellingItems>()
        val res = response
        var selected: ChilditemList? = null
        var totalItem = 0.0
        val binding = CrossSellingDialogBoxBinding.inflate(activity.layoutInflater)
        alertDialog =
            AlertDialog.Builder(activity).setView(binding.root).setCancelable(false).show()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.itemTitle.text = response.description

        binding.itemItemSelected.text =
            "Selection Max ${response.maxSelection}: Min ${response.minSelection}"

        binding.totalCountOfSelectItem.text = "Total Item ${response.childItemList.size}"
        binding.cancelBtn.setOnClickListener {
            alertDialog?.dismiss()
        }
        val crossAdaptor2 = CrossSellingAdaptor {
            if (itemSelected.contains(it)) {
                itemSelected.remove(it)
            } else {
                itemSelected.add(it)
                totalItem += if (checkFieldValue(it.price) || !it.price.isDigitsOnly()) 0.0
                else "%.4f".format(ListOfFoodItemToSearchAdaptor.setPrice(it.price)).toDouble()
            }
            binding.totalCountOfSelectItem.text = "Total Size ${itemSelected.size}"
        }

        val crossAdaptorPart1 = CrossSellingMainUI {
            if (selectionCount <= 0) {
                binding.root.showSandbar("Item Selection Limit Exceed!!")
                return@CrossSellingMainUI
            }
            Utils.createLogcat("TAG_CROSS_SELLING", "ITEM CROSS SELLING -> ${it.childList.size}")
            binding.clearBtn.show()
            binding.submitBtn.text = "ADD"
            binding.submitBtn.show()
            itemSelected.clear()
            selected = it
            binding.itemItemSelected.text =
                it.itemDesc +
                        "\n Selection Max ${it.maxSelection}: Min ${it.minSelection}"
            crossAdaptor2.submitList(it.childList)
            crossAdaptor2.isFlagReset = false
            crossAdaptor2.isEnable = true
            binding.recycleViewItem.adapter = crossAdaptor2
        }

        binding.clearBtn.setOnClickListener {
            itemSelected.clear()
            binding.totalCountOfSelectItem.text = "Total Size ${itemSelected.size}"
            crossAdaptor2.notifyDataSetChanged()
            crossAdaptor2.isFlagReset = true
        }

        binding.clearBtn.hide()
        binding.submitBtn.hide()

        binding.submitBtn.setOnClickListener {
            if (!binding.submitBtn.text.equals("Submit")) {
                selectionCount -= 1
                if (itemSelected.size > selected?.maxSelection?.toLong()!!) {
                    binding.root.showSandbar("Cannot select more then ${selected?.maxSelection?.toLong()!!} items")
                    return@setOnClickListener
                }
                if (itemSelected.size < selected?.minSelection?.toLong()!!) {
                    binding.root.showSandbar("Please select at-least ${selected?.minSelection?.toLong()!!} items")
                    return@setOnClickListener
                }

                binding.itemItemSelected.text =
                    "Selection Max ${response.maxSelection}: Min ${response.minSelection}"
                binding.totalCountOfSelectItem.text = "Total Size ${response.childItemList.size}"
                Utils.createLogcat(
                    "TAG_LAGOUT_ITEM",
                    "ITEM SIZE -> ${itemSelected.size} and ${res.childItemList.first().childList.size} size "
                )
                binding.recycleViewItem.adapter = crossAdaptorPart1
                binding.submitBtn.text = "Submit"


                Utils.createLogcat(
                    "TAG_REMOVED_ITEM_COMPLETED",
                    "ITEM_SELECTED ${selected?.childList?.size}"
                )
                if (itemSelected.isNotEmpty()) {
                    val list = mutableListOf<CrossSellingItems>()
                    itemSelected.forEach {
                        list.add(it)
                    }
                    Utils.createLogcat("TAG_ITEM_COMPLETE", "ITEM IS ITEM $list")
                    selected = selected?.copy(childList = list)
                    if (mutableMainList.contains(selected)) {
                        mutableMainList.remove(selected)
                    }
                    mutableMainList.add(selected!!)
                    Utils.createLogcat("SELECTED_ITEM_LIST", "ITEM_SELECTED ${mutableMainList}")
                }

            } else {
                Utils.createLogcat(
                    "SELECTED_ITEM_LIST",
                    "Successfully completed ${mutableMainList}"
                )
                val cross = CrossSellingJsonResponse(
                    childItemList = mutableMainList,
                    description = response.description,
                    maxSelection = response.maxSelection,
                    minSelection = response.minSelection,
                    parentItem = response.parentItem
                )
                itemClicked?.onItemClicked(Pair(totalItem, cross))
                alertDialog?.dismiss()
            }
        }
        crossAdaptorPart1.submitList(res.childItemList)
        binding.recycleViewItem.adapter = crossAdaptorPart1
        alertDialog?.show()
    }


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    fun displayCrossSellingItem(response: CrossSellingJsonResponse) {
        val binding = CrossSellingDialogBoxBinding.inflate(activity.layoutInflater)

        alertDialog =
            AlertDialog.Builder(activity).setView(binding.root).setCancelable(false).show()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.itemTitle.text = response.description
        binding.itemItemSelected.text = "Total Item Selected ${response.childItemList.size}"

        val mainLs = mutableListOf<CrossSellingItems>()
        response.childItemList.forEach {
            mainLs.addAll(it.childList)
        }
        val crossAdaptor = CrossSellingAdaptor {}
        binding.recycleViewItem.adapter = crossAdaptor
        crossAdaptor.submitList(mainLs)
        crossAdaptor.isFlagReset = true
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