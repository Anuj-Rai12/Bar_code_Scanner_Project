package com.example.mpos.ui.showroom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mpos.R
import com.example.mpos.databinding.ShowRoomFragmentBinding
import com.example.mpos.utils.changeStatusBarColor

class ShowRoomFragment : Fragment(R.layout.show_room_fragment) {

    private lateinit var binding: ShowRoomFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color_two)
        binding = ShowRoomFragmentBinding.bind(view)
    }

}