package com.example.mpos.ui.reservation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mpos.R
import com.example.mpos.databinding.TableReservationLayoutBinding
import com.example.mpos.utils.changeStatusBarColor

class TableReservationFragment :Fragment(R.layout.table_reservation_layout) {
    private lateinit var binding:TableReservationLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color_two)
        binding=TableReservationLayoutBinding.bind(view)
    }

}