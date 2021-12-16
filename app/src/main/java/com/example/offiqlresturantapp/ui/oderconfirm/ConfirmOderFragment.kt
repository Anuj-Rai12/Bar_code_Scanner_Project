package com.example.offiqlresturantapp.ui.oderconfirm

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.ConfirmOrderLayoutBinding
import com.example.offiqlresturantapp.utils.changeStatusBarColor
import com.example.offiqlresturantapp.utils.msg

class ConfirmOderFragment : Fragment(R.layout.confirm_order_layout) {
    private lateinit var binding: ConfirmOrderLayoutBinding

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color)
        binding = ConfirmOrderLayoutBinding.bind(view)
        binding.qrCodeScan.setOnClickListener {
            requireActivity().msg(getString(R.string.scan_btn))
        }
        binding.searchBoxTxt.setOnClickListener {
            //New Fragment
        }
    }
}