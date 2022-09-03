package com.example.motionlyt.ui.auth

import android.os.Bundle
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.CreateAccountFragmentBinding
import com.example.motionlyt.dialog.NotesDialog
import com.example.motionlyt.utils.checkInputValue
import com.example.motionlyt.utils.showSnackBarMsg

class CreateAccountFragment : Fragment(R.layout.create_account_fragment) {
    private lateinit var binding: CreateAccountFragmentBinding
    private val dialog by lazy {
        NotesDialog(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateAccountFragmentBinding.bind(view)
        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.subBtn.setOnClickListener {
            val name = binding.nameEd.text.toString()
            val regNo = binding.regEd.text.toString()
            val courseName = binding.courseEd.text.toString()
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

            msg("Success")

        }

    }

    private fun msg(msg: String) = binding.regEd.showSnackBarMsg(msg)
}