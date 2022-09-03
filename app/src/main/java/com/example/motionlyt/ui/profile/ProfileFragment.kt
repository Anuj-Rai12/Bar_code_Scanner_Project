package com.example.motionlyt.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.motionlyt.NoteActivity
import com.example.motionlyt.R
import com.example.motionlyt.databinding.ProfileFragmentLayoutBinding
import com.example.motionlyt.dialog.NotesDialog
import com.example.motionlyt.model.userinfo.User
import com.example.motionlyt.ui.profile.viewmodel.ProfileViewModel
import com.example.motionlyt.utils.ResponseWrapper

class ProfileFragment : Fragment(R.layout.profile_fragment_layout) {

    private lateinit var binding: ProfileFragmentLayoutBinding

    private val viewModel: ProfileViewModel by viewModels()

    private val dialog by lazy {
        NotesDialog(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileFragmentLayoutBinding.bind(view)
        getResponse()

        binding.logBtn.setOnClickListener {
            dialog.dismiss()
            dialog.logoutDialog(
                "Logout?",
                "Are you sure you want to logout?",
                icon = R.drawable.ic_logout, cancel = {}, success = {
                    (activity as NoteActivity?)?.logout()
                })
        }

    }

    private fun getResponse() {
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
                    val obj = (it.data as User)
                    binding.collegeEd.setText(obj.uni)
                    binding.userNameEd.setText(obj.name)
                    binding.courseEd.setText(obj.coursename)
                    binding.joinEd.setText(obj.joindate)
                    binding.userTitle.text = obj.name?.split("\\s".toRegex())?.get(0)
                    binding.userProfileTxt.text = obj.name?.first()?.uppercaseChar().toString()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserInfo()
        binding.collegeEd.isEnabled = false
        binding.userNameEd.isEnabled = false
        binding.courseEd.isEnabled = false
        binding.joinEd.isEnabled = false
    }

    private fun showErrorDialog(msg: String) {
        dialog.showNormalTxt("Error!!", msg) {}
    }

}