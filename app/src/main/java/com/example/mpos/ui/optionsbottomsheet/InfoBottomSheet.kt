package com.example.mpos.ui.optionsbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mpos.R
import com.example.mpos.adaptor.GenericRecycleViewAdaptor
import com.example.mpos.databinding.InfoLayoutBinding
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoBottomSheet<T>(private val title: String, private val list: List<T>) :
    BottomSheetDialogFragment(), OnBottomSheetClickListener {

    var listener: OnBottomSheetClickListener? = null
    private lateinit var binding: InfoLayoutBinding
    private lateinit var bottomSheetAdaptor: GenericRecycleViewAdaptor<T>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InfoLayoutBinding.inflate(inflater)

        binding.titleBottom.text = title
        setUpRecycleView()
        return binding.root
    }

    private fun setUpRecycleView() {
        binding.optionRecycle.apply {
            bottomSheetAdaptor = GenericRecycleViewAdaptor(list)
            bottomSheetAdaptor.onClickListener = this@InfoBottomSheet
            adapter = bottomSheetAdaptor
        }
    }

    override fun getTheme(): Int {
        return R.style.SheetDialog
    }

    override fun <T> onItemClicked(response: T) {
        dismiss()
        listener?.onItemClicked(response)
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }


}