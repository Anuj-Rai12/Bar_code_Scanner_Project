package com.example.motionlyt.ui.share

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.ShareLayoutFragmentBinding

class SharingFragment : Fragment(R.layout.share_layout_fragment) {
    private lateinit var binding: ShareLayoutFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ShareLayoutFragmentBinding.bind(view)

    }

}