package com.example.offiqlresturantapp.ui.testingconnection

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.TestingConnectionFragmentBinding
import com.example.offiqlresturantapp.utils.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestingConnectionFragment : Fragment(R.layout.testing_connection_fragment) {
    private lateinit var binding: TestingConnectionFragmentBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor()
        binding = TestingConnectionFragmentBinding.bind(view)
        binding.testConnectionId.setOnClickListener {
            val action =
                TestingConnectionFragmentDirections.actionTestingConnectionFragmentToLoginScreenFragment()
            findNavController().navigate(action)
        }
    }
}