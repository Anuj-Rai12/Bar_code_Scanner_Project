package com.example.mpos.ui.restaturantbilling

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mpos.R
import com.example.mpos.databinding.RestaurantBillingFragmentBinding
import com.example.mpos.utils.changeStatusBarColor

class RestaurantBillingFragment : Fragment(R.layout.restaurant_billing_fragment) {
    private lateinit var binding: RestaurantBillingFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color_two)
        binding=RestaurantBillingFragmentBinding.bind(view)
    }
}