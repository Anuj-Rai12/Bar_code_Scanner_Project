package com.example.mpos.ui.deals

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mpos.R
import com.example.mpos.databinding.DealsFragmentLayoutBinding
import com.example.mpos.utils.changeStatusBarColor

class DealsFragment : Fragment(R.layout.deals_fragment_layout) {

    private lateinit var binding: DealsFragmentLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.semi_white_color_two)
        binding = DealsFragmentLayoutBinding.bind(view)

    }

    override fun onResume() {
        super.onResume()
        binding.topAppBar.title="Deals"
    }
}