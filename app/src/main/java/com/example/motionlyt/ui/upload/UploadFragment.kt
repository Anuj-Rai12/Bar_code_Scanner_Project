package com.example.motionlyt.ui.upload

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.motionlyt.R
import com.example.motionlyt.databinding.UploadFragmentLayoutBinding
import com.example.motionlyt.dialog.NotesDialog
import com.example.motionlyt.ui.upload.viewmodel.UploadFragmentViewModel
import com.example.motionlyt.utils.*
import java.io.File

class UploadFragment : Fragment(R.layout.upload_fragment_layout) {
    private lateinit var binding: UploadFragmentLayoutBinding

    private val viewModel: UploadFragmentViewModel by viewModels()

    private var uriFile: String? = null
    private val getUri = registerForActivityResult(GetUriFile()) {
        it.uri?.let { uri ->
            if (it.requestCode) {
                uriFile = uri.toString()
                changeUI()
            }
        }
    }

    private fun changeUI() {
        binding.noItem.hide()
        binding.item.show()
        binding.item.setImageResource(R.drawable.ic_files)
    }


    private val dialog by lazy {
        NotesDialog(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = UploadFragmentLayoutBinding.bind(view)

        viewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { err ->
                showErrorDialog(err)
            }
        }
        binding.noItem.setOnClickListener {
            getUri.launch(InputData(getIntent()))
        }
        getUploadResponse()

        binding.uplaodBtn.setOnClickListener {
            if (uriFile == null) {
                msg("No File found!!")
                return@setOnClickListener
            }
            viewModel.uploadFile(uriFile!!, File(uriFile!!))
        }
        binding.skipBtn.setOnClickListener {
            if (uriFile == null) {
                msg("No File found!!")
                return@setOnClickListener
            }
            removeUI()
            msg("File Removed")
        }
    }

    private fun removeUI() {
        binding.noItem.show()
        binding.item.hide()
        uriFile = null
    }

    private fun getIntent(string: String = "*/*"): Intent {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = string
        return intent
    }


    private fun getUploadResponse() {
        viewModel.fileUpload.observe(viewLifecycleOwner) {
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
                    dialog.showNormalTxt("Success", "${it.data}", cancel = {})
                    removeUI()
                }
            }
        }
    }


    private fun msg(msg: String) = binding.root.showSnackBarMsg(msg)

    private fun showErrorDialog(msg: String) {
        dialog.showNormalTxt("Error!!", msg) {}
    }

    override fun onResume() {
        super.onResume()
        viewModel.setUploadNull()
        dialog.dismiss()
    }
}