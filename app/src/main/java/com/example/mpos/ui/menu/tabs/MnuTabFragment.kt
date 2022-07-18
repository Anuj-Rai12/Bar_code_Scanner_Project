package com.example.mpos.ui.menu.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mpos.R
import com.example.mpos.databinding.MenuFragmentLayoutBinding

class MnuTabFragment constructor(private val title: String) :
    Fragment(R.layout.menu_fragment_layout) {

    private lateinit var binding: MenuFragmentLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MenuFragmentLayoutBinding.bind(view)
        binding.mnuTabTitle.text = title
    }
}