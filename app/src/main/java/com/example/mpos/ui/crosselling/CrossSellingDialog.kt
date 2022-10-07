package com.example.mpos.ui.crosselling

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.example.mpos.R
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.CrossSellingDialogBoxBinding
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CrossSellingDialog(private val activity: Activity) {
    private var alertDialog: AlertDialog? = null
    private val sampleData by lazy {
        listOf(
            ItemMaster(
                id = 23,
                barcode = "100168",
                itemCategory = "ALCOHOLIC",
                itemCode = "100168",
                itemDescription = "item of description is collected",
                itemName = "Corona Rita",
                salePrice = "875",
                uOM = "PORTION",
                decimalAllowed = "true"
            ), ItemMaster(
                id = 33,
                barcode = "100168",
                itemCategory = "ALCOHOLIC",
                itemCode = "100168",
                itemDescription = "item of description is collected",
                itemName = "Corona Rita",
                salePrice = "875",
                uOM = "PORTION",
                decimalAllowed = "true"
            ), ItemMaster(
                id = 24,
                barcode = "100168",
                itemCategory = "ALCOHOLIC",
                itemCode = "100168",
                itemDescription = "item of description is collected",
                itemName = "Corona Rita",
                salePrice = "875",
                uOM = "PORTION",
                decimalAllowed = "true"
            ), ItemMaster(
                id = 22,
                barcode = "100168",
                itemCategory = "ALCOHOLIC",
                itemCode = "100168",
                itemDescription = "item of description is collected",
                itemName = "Corona Rita",
                salePrice = "875",
                uOM = "PORTION",
                decimalAllowed = "true"
            ), ItemMaster(
                id = 25,
                barcode = "100168",
                itemCategory = "ALCOHOLIC",
                itemCode = "100168",
                itemDescription = "item of description is collected",
                itemName = "Corona Rita",
                salePrice = "875",
                uOM = "PORTION",
                decimalAllowed = "true"
            ), ItemMaster(
                id = 13,
                barcode = "100168",
                itemCategory = "ALCOHOLIC",
                itemCode = "100168",
                itemDescription = "item of description is collected",
                itemName = "Corona Rita",
                salePrice = "875",
                uOM = "PORTION",
                decimalAllowed = "true"
            ), ItemMaster(
                id = 253,
                barcode = "100168",
                itemCategory = "ALCOHOLIC",
                itemCode = "100168",
                itemDescription = "item of description is collected",
                itemName = "Corona Rita",
                salePrice = "875",
                uOM = "PORTION",
                decimalAllowed = "true"
            ), ItemMaster(
                id = 203,
                barcode = "100168",
                itemCategory = "ALCOHOLIC",
                itemCode = "100168",
                itemDescription = "item of description is collected",
                itemName = "Corona Rita",
                salePrice = "875",
                uOM = "PORTION",
                decimalAllowed = "true"
            )
        )
    }
    var itemClicked: OnBottomSheetClickListener? = null
    fun showCrossSellingDialog(title: String) {
        val binding = CrossSellingDialogBoxBinding.inflate(activity.layoutInflater)
        alertDialog = MaterialAlertDialogBuilder(
            activity,
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        ).setView(binding.root).setCancelable(false).show()

        binding.itemTitle.text = title
        binding.cancelBtn.setOnClickListener {
            alertDialog?.dismiss()
        }
        val crossAdaptor = CrossSellingAdaptor {
            itemClicked?.onItemClicked(it)
        }
        binding.recycleViewItem.adapter = crossAdaptor
        crossAdaptor.submitList(sampleData)
    }

}