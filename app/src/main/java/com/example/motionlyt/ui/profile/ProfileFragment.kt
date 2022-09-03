package com.example.motionlyt.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.ProfileFragmentLayoutBinding
import com.example.motionlyt.dialog.NotesDialog

class ProfileFragment :Fragment(R.layout.profile_fragment_layout){

    private lateinit var binding:ProfileFragmentLayoutBinding

    private val dialog by lazy {
        NotesDialog(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=ProfileFragmentLayoutBinding.bind(view)


    }

    override fun onResume() {
        super.onResume()
        binding.collegeEd.isEnabled=false
        binding.userNameEd.isEnabled=false
        binding.courseEd.isEnabled=false
        binding.joinEd.isEnabled=false
    }
}