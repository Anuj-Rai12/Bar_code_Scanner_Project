package com.example.offiqlresturantapp.ui.testingconnection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.TestingConnectionFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestingConnectionFragment : Fragment(R.layout.testing_connection_fragment) {
    private lateinit var binding: TestingConnectionFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TestingConnectionFragmentBinding.bind(view)
        binding.testConnectionId.setOnClickListener {
            val action =
                TestingConnectionFragmentDirections.actionTestingConnectionFragmentToLoginScreenFragment()
            findNavController().navigate(action)
        }
    }
}