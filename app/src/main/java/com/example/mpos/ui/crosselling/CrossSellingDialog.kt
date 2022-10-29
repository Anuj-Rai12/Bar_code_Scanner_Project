package com.example.mpos.ui.crosselling

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingJsonResponse
import com.example.mpos.databinding.CrossSellingDialogBoxBinding
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener

class CrossSellingDialog(private val activity: Activity) {
    private var alertDialog: AlertDialog? = null

    var itemClicked: OnBottomSheetClickListener? = null

    fun showCrossSellingDialog(response: CrossSellingJsonResponse) {
        var totalItemSelected = 0
        val binding = CrossSellingDialogBoxBinding.inflate(activity.layoutInflater)

        alertDialog =
            AlertDialog.Builder(activity).setView(binding.root).setCancelable(false).show()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.itemTitle.text = response.description

        binding.itemItemSelected.text =
            "Selection Max ${response.maxSelection}: Min ${response.minSelection}"

        binding.totalCountOfSelectItem.text = "Total Size $totalItemSelected"
        binding.cancelBtn.setOnClickListener {
            alertDialog?.dismiss()
        }
        val crossAdaptor = CrossSellingAdaptor {
            totalItemSelected++
            binding.totalCountOfSelectItem.text = "Total Size $totalItemSelected"
            itemClicked?.onItemClicked(it)
        }
        binding.clearBtn.setOnClickListener {
            totalItemSelected = 0
            binding.totalCountOfSelectItem.text = "Total Size $totalItemSelected"
            crossAdaptor.notifyDataSetChanged()
            crossAdaptor.isFlagReset = true
        }
        binding.submitBtn.setOnClickListener {
            alertDialog?.dismiss()
        }
        binding.recycleViewItem.adapter = crossAdaptor
        crossAdaptor.submitList(response.childItemList)
        alertDialog?.show()
    }

}