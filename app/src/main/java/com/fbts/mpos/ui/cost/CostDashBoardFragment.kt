package com.fbts.mpos.ui.cost

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.fbts.mpos.R
import com.fbts.mpos.databinding.CostCalDashbordLayoutBinding
import com.fbts.mpos.utils.changeStatusBarColor
import com.fbts.mpos.utils.hide
import com.fbts.mpos.utils.show


class CostDashBoardFragment : Fragment(R.layout.cost_cal_dashbord_layout) {
    private lateinit var binding: CostCalDashbordLayoutBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.semi_white_color_two)
        binding = CostCalDashbordLayoutBinding.bind(view)
        binding.restItemBtn.setOnClickListener {
            binding.printerIcClick.hide()
            binding.saveIcClick.hide()
        }
        binding.viewOfferBtn.setOnClickListener {
            binding.printerIcClick.show()
            binding.saveIcClick.show()
        }
    }
}