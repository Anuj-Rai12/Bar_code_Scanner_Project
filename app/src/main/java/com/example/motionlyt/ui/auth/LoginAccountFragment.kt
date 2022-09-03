package com.example.motionlyt.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.LoginAccountFragmentBinding

class LoginAccountFragment :Fragment(R.layout.login_account_fragment){
    private lateinit var binding: LoginAccountFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= LoginAccountFragmentBinding.bind(view)
        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}