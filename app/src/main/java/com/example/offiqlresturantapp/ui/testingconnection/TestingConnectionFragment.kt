package com.example.offiqlresturantapp.ui.testingconnection

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.data.login.model.api.json.ApkLoginJsonResponse
import com.example.offiqlresturantapp.databinding.TestingConnectionFragmentBinding
import com.example.offiqlresturantapp.ui.testingconnection.viewModel.TestingConnectionViewModel
import com.example.offiqlresturantapp.utils.*
import com.github.razir.progressbutton.bindProgressButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestingConnectionFragment : Fragment(R.layout.testing_connection_fragment) {
    private lateinit var binding: TestingConnectionFragmentBinding
    private val viewModel: TestingConnectionViewModel by viewModels()


    private val args: TestingConnectionFragmentArgs by navArgs()

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor()
        binding = TestingConnectionFragmentBinding.bind(view)

        viewModel.events.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { str -> showSnackBar(str) }
        }

        args.bar?.let {
            requireActivity().msg("$it")
        }

        checkApiResponse()

        bindProgressButton(binding.testConnectionId)

        binding.scanOrCodeId.setOnClickListener {
            val action =
                TestingConnectionFragmentDirections.actionGlobalScanQrCodeFragment(Url_barcode)
            findNavController().navigate(action)
        }
        binding.testConnectionId.setOnClickListener {
            val userID = binding.userNameEd.text.toString()
            val password = binding.userPassEd.text.toString()
            val storeNo = binding.storeNoEd.text.toString()
            viewModel.checkLoginTraditional(userID, password, storeNo)
        }
    }

    private fun nextFrag(string: String) {
        val action =
            TestingConnectionFragmentDirections.actionTestingConnectionFragmentToTableManagementOrCostEstimate(
                string
            )
        findNavController().navigate(action)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkApiResponse() {
        viewModel.apk.observe(viewLifecycleOwner) {
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
                        if (json.status) {
                            requireActivity().msg(json.message)
                            nextFrag(json.storeName)
                        } else
                            requireActivity().msg(json.message, Toast.LENGTH_LONG)
                        return@let
                    } ?: showSnackBar("Unable to Login \nCheck Login Credentials")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showSnackBar(msg: String, length: Int = Snackbar.LENGTH_INDEFINITE) {
        binding.root.showSandbar(
            msg,
            length,
            requireActivity().getColorInt(R.color.color_red)
        ) {
            return@showSandbar "OK"
        }
    }

    private fun hideProgress() {
        binding.testConnectionId.hideProgress(getString(R.string.btn_test_con))
    }
}