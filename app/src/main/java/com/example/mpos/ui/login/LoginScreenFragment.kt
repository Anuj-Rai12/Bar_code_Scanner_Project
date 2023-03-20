package com.example.mpos.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mpos.R
import com.example.mpos.data.login.model.api.json.ApkLoginJsonResponse
import com.example.mpos.databinding.LoginScreenFragmentBinding
import com.example.mpos.ui.login.viewmodel.LoginScreenViewModel
import com.example.mpos.ui.searchfood.view_model.SearchFoodViewModel
import com.example.mpos.utils.*
import com.github.razir.progressbutton.bindProgressButton
import com.google.android.material.snackbar.Snackbar


class LoginScreenFragment : Fragment(R.layout.login_screen_fragment) {
    private lateinit var binding: LoginScreenFragmentBinding

    private val viewModel: LoginScreenViewModel by viewModels()
    private val menuItemSyncViewModel: SearchFoodViewModel by viewModels()

    private var jsonLoginResponse: ApkLoginJsonResponse? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor()
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
                activity?.msg("Please Enter Correct Detail")
                return@setOnClickListener
            }
            viewModel.checkLoginTraditional(userID = userName, password = passWord)
        }


        checkAuthCycle()
        syncMenuResponse()
    }

    private fun checkAuthCycle() {
        viewModel.apk.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    hideProgress()
                    activity?.msg("${it.exception?.localizedMessage}")
                    Log.i(TAG, "checkAuthCycle: ${it.exception?.localizedMessage}")
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
                        if (json.status) {
                            jsonLoginResponse = json
                            menuItemSyncViewModel.fetchResponseApi()
                        } else
                            showDialogBox(
                                "Failed!!",
                                "Cannot Login Unauthorized Access${getEmojiByUnicode(0x274C)}\n\nTip ${
                                    getEmojiByUnicode(
                                        0x1F4A1
                                    )
                                },\n${
                                    getEmojiByUnicode(
                                        0x2705
                                    )
                                } Try to check credentials Are correct?\nOr\n${
                                    getEmojiByUnicode(
                                        0x2705
                                    )
                                } Clear the storage of this App and reopen It.",
                                icon = R.drawable.ic_error
                            ) {}
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

    private fun nextFrag(storeNumber: ApkLoginJsonResponse) {
        val item = Bundle()
        item.putParcelable("TBL_VALUE", storeNumber)
        findNavController().navigate(
            R.id.action_loginScreenFragment_to_tableManagementOrCostEstimate,
            item
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun syncMenuResponse() {
        menuItemSyncViewModel.fdInfo.observe(this) {
            when (it) {
                is ApisResponse.Error -> {
                    hideProgress()
                    val err = it.exception?.localizedMessage
                    err?.let { e ->
                        showDialogBox(
                            "Failure",
                            e,
                            icon = R.drawable.ic_error
                        ) {}
                    }
                }
                is ApisResponse.Loading -> {
                    binding.loginBtnId.showButtonProgress(
                        "Syncing menu ${getEmojiByUnicode(0x1F4A1)}...",
                        requireActivity().getColorInt(R.color.white)
                    )
                }
                is ApisResponse.Success -> {
                    hideProgress()
                    jsonLoginResponse?.let { jsn ->
                        nextFrag(jsn)
                    } ?: run {
                        showDialogBox(
                            "Failure",
                            "Cannot Generate Menu Response!!",
                            icon = R.drawable.ic_error
                        ) {}
                    }
                }
            }
        }
    }


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