package com.example.offiqlresturantapp.ui.login

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.data.login.model.api.json.ApkLoginJsonResponse
import com.example.offiqlresturantapp.databinding.LoginScreenFragmentBinding
import com.example.offiqlresturantapp.ui.login.viewmodel.LoginScreenViewModel
import com.example.offiqlresturantapp.utils.*
import com.github.razir.progressbutton.bindProgressButton
import com.google.android.material.snackbar.Snackbar


class LoginScreenFragment : Fragment(R.layout.login_screen_fragment) {
    private lateinit var binding: LoginScreenFragmentBinding

    private val viewModel: LoginScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginScreenFragmentBinding.bind(view)

        viewModel.events.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { msg ->
                showSnackBar(msg)
            }
        }

        bindProgressButton(binding.loginBtnId)

        binding.loginBtnId.setOnClickListener {
            val userName = binding.userNameEd2.text.toString()
            val passWord = binding.userPassEd2.text.toString()
            if (checkFieldValue(userName) || checkFieldValue(passWord)) {
                activity?.msg("Please Enter Correct Password")
                return@setOnClickListener
            }
            viewModel.checkLoginTraditional(userID = userName, password = passWord)
        }


        checkAuthCycle()

    }

    private fun checkAuthCycle() {
        viewModel.apk.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    hideProgress()
                    activity?.msg("${it.exception?.localizedMessage}")
                    showDialogBox(
                        "Failure",
                        "Please Check The Credentials",
                        icon = R.drawable.ic_error
                    ) {}
                }
                is ApisResponse.Loading -> {
                    binding.loginBtnId.showButtonProgress(
                        "${it.data}",
                        requireActivity().getColorInt(R.color.white)
                    )
                }
                is ApisResponse.Success -> {
                    hideProgress()
                    it.data?.let { res ->
                        val json = res as ApkLoginJsonResponse
                        if (json.status)
                            nextFrag(json.storeName)
                    } ?: run {
                        showDialogBox(
                            "Failed!!",
                            "Unknown Error Occur",
                            icon = R.drawable.ic_error
                        ) {}
                    }
                }
            }
        }
    }

    private fun nextFrag(storeNumber: String) {
        val action =
            LoginScreenFragmentDirections.actionLoginScreenFragmentToTableManagementOrCostEstimate(
                storeNumber
            )
        findNavController().navigate(action)
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
        binding.loginBtnId.hideProgress(getString(R.string.btn_login))
    }


}