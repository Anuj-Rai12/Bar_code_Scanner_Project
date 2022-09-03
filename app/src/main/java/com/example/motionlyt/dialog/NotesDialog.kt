package com.example.motionlyt.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.motionlyt.databinding.PbLoadingDialogBinding
import com.example.motionlyt.databinding.ShowDialogMsgBinding
import com.example.motionlyt.utils.show

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


    fun showNormalTxt(
        title: String,
        msg: String,
        isCancel: Boolean = true,
        icon: Int? = null,
        cancel: () -> Unit
    ): NotesDialog {
        val binding = ShowDialogMsgBinding.inflate(context.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(isCancel)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.titleTxt.text = title
        binding.descTxt.text = msg
        icon?.let {
            binding.icImg.setImageResource(it)
        }
        binding.disBtn.setOnClickListener {
            cancel.invoke()
            dialog.dismiss()
        }
        dialog.show()
        return this
    }


    fun logoutDialog(
        title: String,
        msg: String,
        isCancel: Boolean = true,
        icon: Int? = null,
        cancel: () -> Unit,
        success: () -> Unit
    ): NotesDialog {
        val binding = ShowDialogMsgBinding.inflate(context.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(isCancel)
        binding.layoutBtn.show()
        binding.disBtn.text="Cancel"
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.titleTxt.text = title
        binding.descTxt.text = msg
        icon?.let {
            binding.icImg.setImageResource(it)
        }
        binding.disBtn.setOnClickListener {
            cancel.invoke()
            dialog.dismiss()
        }
        binding.layoutBtn.setOnClickListener {
            success.invoke()
            dialog.dismiss()
        }
        dialog.show()
        return this
    }


    fun dismiss() {
        dialog.dismiss()
    }

}