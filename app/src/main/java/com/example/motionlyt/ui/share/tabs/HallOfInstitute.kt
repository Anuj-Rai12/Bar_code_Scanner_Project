package com.example.motionlyt.ui.share.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.HallOfInstruitionFragmentBinding

class HallOfInstitute :Fragment(R.layout.hall_of_instruition_fragment) {
    private lateinit var binding:HallOfInstruitionFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= HallOfInstruitionFragmentBinding.bind(view)
    }

}