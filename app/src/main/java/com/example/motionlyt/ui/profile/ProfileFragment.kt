package com.example.motionlyt.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.ProfileFragmentLayoutBinding

class ProfileFragment :Fragment(R.layout.profile_fragment_layout){

    private lateinit var binding:ProfileFragmentLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=ProfileFragmentLayoutBinding.bind(view)

    }

}