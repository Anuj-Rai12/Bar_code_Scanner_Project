package com.example.motionlyt.ui.share.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.AllSharedFragmentBinding

class SharedFragment :Fragment(R.layout.all_shared_fragment){
    private lateinit var binding:AllSharedFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= AllSharedFragmentBinding.bind(view)
    }

}