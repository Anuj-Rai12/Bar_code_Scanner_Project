package com.example.offiqlresturantapp.ui.testingconnection

import android.annotation.SuppressLint
import android.graphics.Color
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
import com.example.offiqlresturantapp.ui.testingconnection.viewModel.TestingConnectionViewModel
import com.example.offiqlresturantapp.utils.*
import com.github.razir.progressbutton.bindProgressButton
import com.google.android.material.snackbar.Snackbar


class TestingConnectionFragment : Fragment(R.layout.testing_connection_fragment) {
    private lateinit var binding: TestingConnectionFragmentBinding
    private val viewModel: TestingConnectionViewModel by viewModels()

    private val args: TestingConnectionFragmentArgs by navArgs()

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor()
        binding = TestingConnectionFragmentBinding.bind(view)

        viewModel.events.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { str ->
                if (str == "Please Scan Url") {
                    getScannedUrl()
                } else
                    showSnackBar(str)
            }
        }

        args.bar?.let {
            try {
                if (it.title != null && it.uri != null) {
                    activity?.msg("${it.uri}")
                    viewModel.scannerUrl = it.uri
                } else {
                    Log.i(TAG, "onViewCreated: url is Null")
                }
            } catch (e: Exception) {
                Log.i(TAG, "onViewCreated: ${e.localizedMessage}")
            }
        }

        checkApiResponse()

        bindProgressButton(binding.testConnectionId)

        binding.scanOrCodeId.setOnClickListener {
            getScannedUrl()
        }
        binding.testConnectionId.setOnClickListener {
            val userID = binding.userNameEd.text.toString()
            val password = binding.userPassEd.text.toString()
            val storeNo = binding.storeNoEd.text.toString()
            if (checkFieldValue(userID) || checkFieldValue(password) || checkFieldValue(storeNo)) {
                activity?.msg("Please Enter the Details Correctly")
                return@setOnClickListener
            }
            viewModel.testTheUrl(userID.trim(), password.trim(), storeNo.trim())
        }
    }

    private fun getScannedUrl() {
        activity?.showDialogBoxToGetUrl(scan = {
            nextFragForScan()
        }, done = { res ->
            viewModel.scannerUrl = res
        })
    }

    private fun nextFragForScan() {
        val action =
            TestingConnectionFragmentDirections.actionGlobalScanQrCodeFragment(
                Url_barcode,
                null,
                null,
                null
            )
        findNavController().navigate(action)
    }

    private fun nextFrag() {
        val action =
            TestingConnectionFragmentDirections.actionTestingConnectionFragmentToLoginScreenFragment()
        findNavController().navigate(action)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkApiResponse() {
        viewModel.testingConnection.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    hideProgress()
                    activity?.msg("${it.exception?.localizedMessage}")
                    showDialogBox(
                        "Failed!!",
                        "Please Check the Credentials or Try Again!!",
                        icon = R.drawable.ic_error
                    ) {}
                }
                is ApisResponse.Loading -> {
                    binding.testConnectionId.showButtonProgress(
                        "${it.data}",
                        activity?.getColorInt(R.color.white) ?: Color.WHITE
                    )
                }
                is ApisResponse.Success -> {
                    hideProgress()
                    it.data?.let {
                        nextFrag()
                    } ?: run {
                        showDialogBox(
                            "Failed!!",
                            "Please Check the Credentials or Try Again!!",
                            icon = R.drawable.ic_error
                        ) {}
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showSnackBar(msg: String, length: Int = Snackbar.LENGTH_INDEFINITE) {
        binding.root.showSandbar(
            msg,
            length,
            activity?.getColorInt(R.color.color_red) ?: Color.RED
        ) {
            return@showSandbar "OK"
        }
    }

    private fun hideProgress() {
        binding.testConnectionId.hideProgress(getString(R.string.btn_test_con))
    }
}