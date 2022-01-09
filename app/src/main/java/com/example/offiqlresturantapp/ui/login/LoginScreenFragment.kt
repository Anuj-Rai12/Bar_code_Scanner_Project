package com.example.offiqlresturantapp.ui.login

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.LoginScreenFragmentBinding
import com.example.offiqlresturantapp.utils.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreenFragment : Fragment(R.layout.login_screen_fragment) {
    private lateinit var binding: LoginScreenFragmentBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor()
        binding = LoginScreenFragmentBinding.bind(view)
        binding.loginBtnId.setOnClickListener {
            /*val action =
                LoginScreenFragmentDirections.actionLoginScreenFragmentToTableManagementOrCostEstimate()
            findNavController().navigate(action)*/
        }
    }
}