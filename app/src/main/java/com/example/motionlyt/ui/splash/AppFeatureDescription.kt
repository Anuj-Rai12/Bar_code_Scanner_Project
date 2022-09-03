package com.example.motionlyt.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.AppFeatureDescBinding

class AppFeatureDescription(
    private val image: Int,
    private val title: String,
    private val desc: String
) :
    Fragment(R.layout.app_feature_desc) {
    private lateinit var binding: AppFeatureDescBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AppFeatureDescBinding.bind(view)

        binding.featureIcon.setImageResource(image)
        binding.headerDesc.text = desc
        binding.headerText.text = title

    }

}