package com.example.offiqlresturantapp.ui.testingconnection

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.TestingConnectionFragmentBinding
import com.example.offiqlresturantapp.utils.Url_barcode
import com.example.offiqlresturantapp.utils.changeStatusBarColor
import com.example.offiqlresturantapp.utils.msg
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.InvocationTargetException

@AndroidEntryPoint
class TestingConnectionFragment : Fragment(R.layout.testing_connection_fragment) {
    private lateinit var binding: TestingConnectionFragmentBinding

    //private val args: TestingConnectionFragmentArgs by navArgs()
    private val args by lazy {
        try {
            navArgs<TestingConnectionFragmentArgs>().value
        } catch (e: Exception) {
            null
        } catch (e: InvocationTargetException) {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor()
        binding = TestingConnectionFragmentBinding.bind(view)
        args?.bar?.let {
            requireActivity().msg("$it")
        }

        binding.scanOrCodeId.setOnClickListener {
            val action =
                TestingConnectionFragmentDirections.actionGlobalScanQrCodeFragment(Url_barcode)
            findNavController().navigate(action)
        }
        binding.testConnectionId.setOnClickListener {
            val action =
                TestingConnectionFragmentDirections.actionTestingConnectionFragmentToLoginScreenFragment()
            findNavController().navigate(action)
        }
    }
}