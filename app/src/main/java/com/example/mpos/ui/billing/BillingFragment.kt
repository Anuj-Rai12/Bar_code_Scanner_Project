package com.example.mpos.ui.billing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mpos.R
import com.example.mpos.databinding.BillingFragmentLayoutBinding

class BillingFragment : Fragment(R.layout.billing_fragment_layout) {
    private lateinit var binding: BillingFragmentLayoutBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BillingFragmentLayoutBinding.bind(view)
    }
}