package com.example.offiqlresturantapp.ui.testingconnection

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.databinding.TestingConnectionFragmentBinding
import com.example.offiqlresturantapp.ui.testingconnection.model.api.ApKLoginPost
import com.example.offiqlresturantapp.ui.testingconnection.model.api.ApkBody
import com.example.offiqlresturantapp.ui.testingconnection.model.api.json.ApkLoginJsonResponse
import com.example.offiqlresturantapp.ui.testingconnection.viewModel.TestingConnectionViewModel
import com.example.offiqlresturantapp.utils.*
import com.github.razir.progressbutton.bindProgressButton
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.InvocationTargetException
import javax.inject.Inject

@AndroidEntryPoint
class TestingConnectionFragment : Fragment(R.layout.testing_connection_fragment) {
    private lateinit var binding: TestingConnectionFragmentBinding
    private val viewModel: TestingConnectionViewModel by viewModels()

    @Inject
    lateinit var userSoredData: UserSoredData

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
    override fun onStart() {
        userSoredData.read.asLiveData().observe(viewLifecycleOwner) {
            if (it != null && !checkFieldValue(it.userID!!)) {
                val data = ApKLoginPost(apK = it)
                checkApiResponse(data, false)
            }
        }
        super.onStart()
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
            val userID = binding.userNameEd.text.toString()
            val password = binding.userPassEd.text.toString()
            val storeNo=binding.storeNoEd.text.toString()
            if (checkFieldValue(userID) || checkFieldValue(password)|| checkFieldValue(storeNo)) {
                requireActivity().msg("Please Enter the Correct Info")
            } else {
                val data = ApKLoginPost(
                    apK = ApkBody(
                        storeNo = storeNo,
                        userID = userID,
                        password = password
                    )
                )
                checkApiResponse(data)
            }
        }
    }

    private fun nextFrag(string: String) {
        val action =
            TestingConnectionFragmentDirections.actionTestingConnectionFragmentToTableManagementOrCostEstimate(string)
        findNavController().navigate(action)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkApiResponse(data: ApKLoginPost, flag: Boolean = true) {
        viewModel.getApkLoginResponse(data, flag).observe(viewLifecycleOwner) {
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
                        if (json.status && flag) {
                            requireActivity().msg(json.message)
                            nextFrag(json.storeName)
                        } else if (!json.status)
                            requireActivity().msg(json.message, Toast.LENGTH_LONG)
                        else if (json.status)
                            nextFrag(json.storeName)
                    }
                }
            }
        }
    }

    private fun hideProgress() {
        binding.testConnectionId.hideProgress(getString(R.string.btn_test_con))
    }
}