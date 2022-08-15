package com.example.mpos.ui.billing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mpos.R
import com.example.mpos.databinding.BillingFragmentLayoutBinding
import com.example.mpos.utils.changeStatusBarColor

class BillingFragment : Fragment(R.layout.billing_fragment_layout) {
    private lateinit var binding: BillingFragmentLayoutBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.semi_white_color_two)
        binding = BillingFragmentLayoutBinding.bind(view)
        //Dow
    }
}