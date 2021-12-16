package com.example.offiqlresturantapp.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.LoginScreenFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreenFragment : Fragment(R.layout.login_screen_fragment) {
    private lateinit var binding: LoginScreenFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginScreenFragmentBinding.bind(view)
        binding.loginBtnId.setOnClickListener {
            val action =
                LoginScreenFragmentDirections.actionLoginScreenFragmentToTableManagementOrCostEstimate()
            findNavController().navigate(action)
        }
    }
}