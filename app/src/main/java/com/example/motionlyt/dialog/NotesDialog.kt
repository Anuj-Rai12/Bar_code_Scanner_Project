package com.example.motionlyt.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.motionlyt.databinding.PbLoadingDialogBinding

class NotesDialog(private val context: Activity) {

    private val dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    fun showDialogLoading(msg: String): NotesDialog {
        val binding = PbLoadingDialogBinding.inflate(context.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.titleTxt.text = msg
        dialog.show()
        return this
    }

    fun dismiss() {
        dialog.dismiss()
    }

}