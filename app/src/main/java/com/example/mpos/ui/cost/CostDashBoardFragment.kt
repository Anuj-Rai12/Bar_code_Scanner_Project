package com.example.mpos.ui.cost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mpos.R
import com.example.mpos.databinding.CostCalDashbordLayoutBinding
import com.example.mpos.utils.changeStatusBarColor


class CostDashBoardFragment : Fragment(R.layout.cost_cal_dashbord_layout) {
    private lateinit var binding: CostCalDashbordLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.semi_white_color_two)
        binding = CostCalDashbordLayoutBinding.bind(view)

    }
}