package com.example.motionlyt.ui.auth

import android.os.Bundle
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.motionlyt.LoginActivity
import com.example.motionlyt.R
import com.example.motionlyt.databinding.CreateAccountFragmentBinding
import com.example.motionlyt.dialog.NotesDialog
import com.example.motionlyt.model.userinfo.User
import com.example.motionlyt.ui.auth.viewmodel.LoginViewModel
import com.example.motionlyt.utils.ResponseWrapper
import com.example.motionlyt.utils.checkInputValue
import com.example.motionlyt.utils.getDate
import com.example.motionlyt.utils.showSnackBarMsg

class CreateAccountFragment : Fragment(R.layout.create_account_fragment) {
    private lateinit var binding: CreateAccountFragmentBinding
    private val viewModel: LoginViewModel by viewModels()
    private val dialog by lazy {
        NotesDialog(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateAccountFragmentBinding.bind(view)

        viewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { err ->
                showErrorDialog(err)
            }
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.subBtn.setOnClickListener {
            val name = binding.nameEd.text.toString()
            val regNo = binding.regEd.text.toString()
            val courseName = binding.courseEd.text.toString()
            val pass = binding.passEd.text.toString()
            val uni = binding.uniEd.text.toString()
            if (checkInputValue(name)) {
                msg("Please Add your name")
                return@setOnClickListener
            }

            if (checkInputValue(regNo) || !regNo.isDigitsOnly()) {
                msg("Please Add your valid regNo")
                return@setOnClickListener
            }

            if (checkInputValue(courseName)) {
                msg("Please Add your Course Name")
                return@setOnClickListener
            }

            if (checkInputValue(pass) || pass
                    .length <= 6
            ) {
                msg("Invalid Pass")
                return@setOnClickListener
            }
            if (checkInputValue(uni)) {
                msg("Please Add your university name")
                return@setOnClickListener
            }
            viewModel.setUserAccount(User(name.trim(), regNo.trim(), uni.trim(), courseName.trim(), pass.trim(), getDate()))
        }

        getCreateUserResponse()
    }

    private fun getCreateUserResponse() {
        viewModel.userAcc.observe(viewLifecycleOwner) {
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
                    (activity as LoginActivity?)?.goToNote()
                }
            }
        }
    }

    private fun msg(msg: String) = binding.root.showSnackBarMsg(msg)

    private fun showErrorDialog(msg: String) {
        dialog.showNormalTxt("Error!!", msg) {}
    }
}