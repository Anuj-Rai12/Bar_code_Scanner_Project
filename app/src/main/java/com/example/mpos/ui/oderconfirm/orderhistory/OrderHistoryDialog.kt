package com.example.mpos.ui.oderconfirm.orderhistory

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.example.mpos.R
import com.example.mpos.data.table_info.model.json.TableDetail
import com.example.mpos.databinding.OrderHistoryDialogBoxBinding
import com.example.mpos.ui.oderconfirm.adaptor.ConfirmOderFragmentAdaptor
import com.example.mpos.ui.oderconfirm.view_model.ConfirmOrderFragmentViewModel
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.utils.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun Activity.showDialogForOrderHistory(
    title: String,
    tableDetail: TableDetail,
    viewLifecycleOwner: LifecycleOwner,
    viewModel: ConfirmOrderFragmentViewModel,
    cancel: (Boolean) -> Unit
) {
    lateinit var confirmOderFragmentAdaptor: ConfirmOderFragmentAdaptor

    val binding = OrderHistoryDialogBoxBinding.inflate(layoutInflater)

    val materialDialogs = MaterialAlertDialogBuilder(
        this,
        R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog_simple
    )
        .setView(binding.root)
        .setCancelable(false)
        .show()

    binding.orderHistoryTxt.text = title
    binding.orderRecycleView.apply {
        confirmOderFragmentAdaptor = ConfirmOderFragmentAdaptor({},{})
        confirmOderFragmentAdaptor.setCheckBoxType(false)
        adapter = confirmOderFragmentAdaptor
    }

    binding.closeBtn.setOnClickListener {
        cancel.invoke(false)
        materialDialogs.dismiss()
    }

    viewModel.getOccupiedTableItem(tableDetail)


    binding.printBtn.setOnClickListener {
        cancel.invoke(true)
        materialDialogs.dismiss()
    }

    viewModel.occupiedTbl.observe(viewLifecycleOwner) {
        when (it) {
            is ApisResponse.Error -> {
                binding.pbLayout.root.hide()
                if (it.data == null) {
                    it.exception?.localizedMessage?.let { err ->
                        msg(err, Toast.LENGTH_LONG)
                    }
                } else {
                    msg("${it.data}", Toast.LENGTH_LONG)
                }
            }
            is ApisResponse.Loading -> {
                binding.pbLayout.titleTxt.text = "${it.data}"
                binding.pbLayout.root.show()
            }
            is ApisResponse.Success -> {
                binding.pbLayout.root.hide()
                binding.orderRecycleViewHint.hide()
                binding.orderRecycleView.show()
                (it.data as FoodItemList?)?.let { item ->
                    viewModel.getGrandTotal(item.foodList)
                    if (item.foodList.isNotEmpty()) {
                        confirmOderFragmentAdaptor.notifyDataSetChanged()
                        confirmOderFragmentAdaptor.submitList(item.foodList)
                    } else
                        msg("No order found!! ${getEmojiByUnicode(0x1F615)}")
                }
            }
        }
    }

    viewModel.grandTotal.observe(viewLifecycleOwner) {
        binding.totalOrderAmt.text = it
    }

}