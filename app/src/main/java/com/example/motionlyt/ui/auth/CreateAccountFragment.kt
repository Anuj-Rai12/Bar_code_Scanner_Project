package com.example.motionlyt.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.CreateAccountFragmentBinding

class CreateAccountFragment : Fragment(R.layout.create_account_fragment) {
    private lateinit var binding: CreateAccountFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateAccountFragmentBinding.bind(view)

    }
}