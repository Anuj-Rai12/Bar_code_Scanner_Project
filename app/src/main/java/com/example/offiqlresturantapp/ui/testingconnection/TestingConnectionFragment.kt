package com.example.offiqlresturantapp.ui.testingconnection

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.TestingConnectionFragmentBinding
import com.example.offiqlresturantapp.ui.testingconnection.model.api.ApKLoginPost
import com.example.offiqlresturantapp.ui.testingconnection.model.api.ApkBody
import com.example.offiqlresturantapp.ui.testingconnection.model.api.json.ApkLoginJsonResponse
import com.example.offiqlresturantapp.ui.testingconnection.viewModel.TestingConnectionViewModel
import com.example.offiqlresturantapp.utils.*
import com.github.razir.progressbutton.bindProgressButton
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.InvocationTargetException

@AndroidEntryPoint
class TestingConnectionFragment : Fragment(R.layout.testing_connection_fragment) {
    private lateinit var binding: TestingConnectionFragmentBinding
    private val viewModel: TestingConnectionViewModel by viewModels()
    private val args by lazy {
        try {
            navArgs<TestingConnectionFragmentArgs>().value
        } catch (e: Exception) {
            null
        } catch (e: InvocationTargetException) {
            null
        }
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor()
        binding = TestingConnectionFragmentBinding.bind(view)
        args?.bar?.let {
            requireActivity().msg("$it")
        }

        bindProgressButton(binding.testConnectionId)

        binding.scanOrCodeId.setOnClickListener {
            val action =
                TestingConnectionFragmentDirections.actionGlobalScanQrCodeFragment(Url_barcode)
            findNavController().navigate(action)
        }
        binding.testConnectionId.setOnClickListener {
            val userName = binding.userNameEd.text.toString()
            val password = binding.userPassEd.text.toString()
            if (checkFieldValue(userName) || checkFieldValue(password)) {
                requireActivity().msg("Please Enter the Correct Info")
            } else {
                val data = ApKLoginPost(
                    apK = ApkBody(
                        storeNo = "RO404",
                        userID = userName,
                        password = password
                    )
                )
                checkApiResponse(data)
            }
        }
    }

    private fun nextFrag() {
        val action =
            TestingConnectionFragmentDirections.actionTestingConnectionFragmentToTableManagementOrCostEstimate()
        findNavController().navigate(action)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkApiResponse(data: ApKLoginPost) {
        viewModel.getApkLoginResponse(data).observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    hideProgress()
                    requireActivity().msg("${it.exception?.localizedMessage}")
                }
                is ApisResponse.Loading -> {
                    binding.testConnectionId.showButtonProgress(
                        "${it.data}",
                        requireActivity().getColorInt(R.color.white)
                    )
                }
                is ApisResponse.Success -> {
                    hideProgress()
                    it.data?.let { type ->
                        val json = type as ApkLoginJsonResponse
                        requireActivity().msg("Success Login")
                        Log.i(TAG, "checkApiResponse: $json")
                    }
                    //Log.i(TAG, "checkApiResponse: ${it.data}")
                }
            }
        }
    }

    private fun hideProgress() {
        binding.testConnectionId.hideProgress(getString(R.string.btn_test_con))
    }
}