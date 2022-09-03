package com.example.motionlyt.ui.upload

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.UploadFragmentLayoutBinding

class UploadFragment : Fragment(R.layout.upload_fragment_layout) {

    private lateinit var binding: UploadFragmentLayoutBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = UploadFragmentLayoutBinding.bind(view)

    }

}