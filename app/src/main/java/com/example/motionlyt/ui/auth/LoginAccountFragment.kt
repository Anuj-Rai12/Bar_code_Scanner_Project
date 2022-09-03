package com.example.motionlyt.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.motionlyt.R
import com.example.motionlyt.databinding.LoginAccountFragmentBinding
import com.example.motionlyt.dialog.NotesDialog
import com.example.motionlyt.ui.auth.viewmodel.LoginViewModel
import com.example.motionlyt.utils.ResponseWrapper
import com.example.motionlyt.utils.checkInputValue
import com.example.motionlyt.utils.showSnackBarMsg

class LoginAccountFragment : Fragment(R.layout.login_account_fragment) {
    private lateinit var binding: LoginAccountFragmentBinding
    private val dialog by lazy {
        NotesDialog(requireActivity())
    }

    private val viewModel: LoginViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginAccountFragmentBinding.bind(view)

        viewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { err ->
                dialog.showNormalTxt("Error!!", err) {}
            }
        }
        getUserValidResponse()
        binding.goToNextScr.setOnClickListener {
            val reg = binding.regNo.text.toString()
            val pass = binding.passNo.text.toString()

            if (checkInputValue(reg)) {
                msg("Invalid Reg No!!")
                return@setOnClickListener
            }

            if (checkInputValue(pass)) {
                msg("Invalid Password!!")
                return@setOnClickListener
            }
            viewModel.isUserIsValid(reg, pass)
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    private fun getUserValidResponse() {
        viewModel.userValid.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseWrapper.Error -> {
                    dialog.dismiss()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { err ->
                            showErrorDialog(err)
                        }
                    } else {
                        showErrorDialog("${it.data}")
                    }
                }
                is ResponseWrapper.Loading -> {
                    dialog.showDialogLoading("${it.data}")
                }
                is ResponseWrapper.Success -> {
                    dialog.dismiss()
                    msg("Success!!")
                }
            }
        }
    }


    private fun msg(msg: String) = binding.root.showSnackBarMsg(msg)

    private fun showErrorDialog(msg: String) {
        dialog.showNormalTxt("Error!!", msg) {}
    }

}