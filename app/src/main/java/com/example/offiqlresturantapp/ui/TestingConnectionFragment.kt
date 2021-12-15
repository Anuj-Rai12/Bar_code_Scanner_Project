package com.example.offiqlresturantapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.TestingConnectionFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestingConnectionFragment : Fragment(R.layout.testing_connection_fragment) {
    private lateinit var binding: TestingConnectionFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TestingConnectionFragmentBinding.bind(view)

    }
}