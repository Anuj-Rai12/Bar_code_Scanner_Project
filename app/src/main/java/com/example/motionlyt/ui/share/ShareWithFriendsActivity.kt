package com.example.motionlyt.ui.share

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.motionlyt.R
import com.example.motionlyt.databinding.ShareWithFriendsLayoutBinding
import com.example.motionlyt.dialog.NotesDialog
import com.example.motionlyt.model.data.FileDataClass
import com.example.motionlyt.model.userinfo.User
import com.example.motionlyt.ui.share.adaptor.FriendsAdaptor
import com.example.motionlyt.ui.share.viewmodel.ShareViewModel
import com.example.motionlyt.utils.ResponseWrapper
import com.example.motionlyt.utils.changeStatusBarColor
import com.example.motionlyt.utils.hide
import com.example.motionlyt.utils.toastMsg

class ShareWithFriendsActivity : AppCompatActivity() {

    private val binding by lazy {
        ShareWithFriendsLayoutBinding.inflate(layoutInflater)
    }
    private val viewModel: ShareViewModel by viewModels()
    private val dialog by lazy {
        NotesDialog(this)
    }


    private val getArr = mutableListOf<User>()

    private lateinit var fileDataAdaptor: FriendsAdaptor


    private val extras by lazy {
        intent.getSerializableExtra("user") as FileDataClass?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        viewModel.event.observe(this) {
            it.getContentIfNotHandled()?.let { err ->
                showErrorDialog(err)
            }
        }
        setAdaptor()
        viewModel.getListOfUser()
        toastMsg("${extras?.size}")
        fileDataAdaptor.userList.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.layoutBtn.text = "all"
            } else {
                binding.layoutBtn.text = "${it.size}"
            }
        }

        binding.layoutBtn.setOnClickListener {
            if (binding.layoutBtn.text == "all") {

            } else {

            }
        }

        getResponse()

    }

    private fun getResponse() {
        viewModel.fileUpload.observe(this) {
            when (it) {
                is ResponseWrapper.Error -> {
                    dialog.dismiss()
                    it.exception?.localizedMessage?.let { err ->
                        showErrorDialog(err)
                    }
                }
                is ResponseWrapper.Loading -> {
                    dialog.showDialogLoading("${it.data}")
                }
                is ResponseWrapper.Success -> {
                    dialog.dismiss()
                    val list = it.data as MutableList<User>
                    if (list.isEmpty()) {
                        showErrorDialog("No Data Found!!")
                    } else {
                        getArr.clear()
                        getArr.addAll(list)
                        fileDataAdaptor.submitList(list)
                    }
                }
            }
        }
    }

    private fun setAdaptor() {
        binding.userList.apply {
            fileDataAdaptor = FriendsAdaptor()
            adapter = fileDataAdaptor
        }
    }

    override fun onResume() {
        super.onResume()
        this.hide()
        this.changeStatusBarColor(R.color.semi_white_color_two)
    }

    private fun showErrorDialog(msg: String) {
        dialog.showNormalTxt("Error!!", msg) {}
    }

}