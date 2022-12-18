package com.example.mpos.ui.testingconnection

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mpos.R
import com.example.mpos.databinding.TestingConnectionFragmentBinding
import com.example.mpos.ui.testingconnection.viewModel.TestingConnectionViewModel
import com.example.mpos.utils.*
import com.github.razir.progressbutton.bindProgressButton
import com.google.android.material.snackbar.Snackbar


class TestingConnectionFragment : Fragment(R.layout.testing_connection_fragment) {
    private lateinit var binding: TestingConnectionFragmentBinding
    private val viewModel: TestingConnectionViewModel by viewModels()
    private val args: TestingConnectionFragmentArgs by navArgs()

    private var isDialogForUrlOpen = false

    @SuppressLint("ResourceType")
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
        if (!isDialogForUrlOpen) {
            isDialogForUrlOpen = true
            activity?.showDialogBoxToGetUrl(scan = {
                isDialogForUrlOpen = false
                nextFragForScan()
            }, done = { res ->
                isDialogForUrlOpen = false
                viewModel.scannerUrl = res
                activity?.msg("${viewModel.scannerUrl}")
            })
        }
    }

    private fun nextFragForScan() {
        val action =
            TestingConnectionFragmentDirections.actionGlobalScanQrCodeFragment(
                Url_barcode,
                null,
                null,
                null, WhereToGoFromScan.TESTINGCONNECTION.name, null
            )
        findNavController().safeNavigate(action)
    }

    private fun nextFrag() {
        val action =
            TestingConnectionFragmentDirections.actionTestingConnectionFragmentToLoginScreenFragment()
        findNavController().safeNavigate(action)
    }


    private fun checkApiResponse() {
        viewModel.testingConnection.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    hideProgress()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { err ->
                            showErrorDialog(err)
                        }
                    } else {
                        showErrorDialog("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    binding.testConnectionId.showButtonProgress(
                        "${it.data}",
                        activity?.getColorInt(R.color.white) ?: Color.WHITE
                    )
                }
                is ApisResponse.Success -> {
                    hideProgress()
                    nextFrag()
                }
            }
        }
    }

    private fun showErrorDialog(desc: String) {
        showDialogBox(
            "Failed!!",
            desc,
            icon = R.drawable.ic_error
        ) {}
    }


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