package com.example.mpos.ui.showroombilling

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mpos.R
import com.example.mpos.databinding.ShowRoomBillingFragmentBinding
import com.example.mpos.utils.changeStatusBarColor

class ShowRoomBillingFragment :Fragment(R.layout.show_room_billing_fragment) {
    private lateinit var binding: ShowRoomBillingFragmentBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color_two)
        binding= ShowRoomBillingFragmentBinding.bind(view)
    }


}