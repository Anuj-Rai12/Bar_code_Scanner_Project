package com.example.mpos.ui.menu.tabs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mpos.R
import com.example.mpos.databinding.MenuFragmentLayoutBinding
import com.example.mpos.ui.menu.bottomsheet.MenuBottomSheetFragment
import com.example.mpos.utils.TAG

class MnuTabFragment constructor(private val title: String) :
    Fragment(R.layout.menu_fragment_layout) {

    private lateinit var binding: MenuFragmentLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MenuFragmentLayoutBinding.bind(view)
        val op = (parentFragment as MenuBottomSheetFragment).getMnuResponse()
        Log.i(TAG, "onViewCreated: $op")
    }
}