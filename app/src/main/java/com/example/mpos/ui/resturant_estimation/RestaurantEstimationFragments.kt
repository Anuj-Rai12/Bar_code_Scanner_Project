package com.example.mpos.ui.resturant_estimation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mpos.R
import com.example.mpos.databinding.RestaurantEstimationFragmentBinding
import com.example.mpos.utils.changeStatusBarColor

class RestaurantEstimationFragments : Fragment(R.layout.restaurant_estimation_fragment) {
    private lateinit var binding: RestaurantEstimationFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color_two)
        binding = RestaurantEstimationFragmentBinding.bind(view)
    }

}