package com.example.motionlyt.ui.share.tabs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.motionlyt.R
import com.example.motionlyt.databinding.MyFileFragmentLayoutBinding
import com.example.motionlyt.dialog.NotesDialog
import com.example.motionlyt.model.data.FileData
import com.example.motionlyt.ui.share.SharingFragment
import com.example.motionlyt.ui.share.adaptor.FileDataAdaptor
import com.example.motionlyt.ui.share.viewmodel.ShareViewModel
import com.example.motionlyt.utils.ResponseWrapper

class MyFileFragment : Fragment(R.layout.my_file_fragment_layout) {


    private val viewModel: ShareViewModel by viewModels()

    private lateinit var adaptor: FileDataAdaptor
    private lateinit var binding: MyFileFragmentLayoutBinding

    private val dialog by lazy {
        NotesDialog(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MyFileFragmentLayoutBinding.bind(view)
        viewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { err ->
                showErrorDialog(err)
            }
        }
        setRecycle()
        getFile()
    }

    @SuppressLint("UNCHECKED_CAST")
    private fun getFile() {
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
                    val list = it.data as MutableList<FileData>

                    if (list.isEmpty()) {
                        showErrorDialog("No Data found!!")
                    } else {
                        adaptor.submitList(list)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFileUpload()
    }

    private fun setRecycle() {
        binding.recycleView.apply {
            this@MyFileFragment.adaptor = FileDataAdaptor {
                (parentFragment as SharingFragment?)?.getUser(it)
            }
            adapter = this@MyFileFragment.adaptor
        }
    }


    private fun showErrorDialog(msg: String) {
        dialog.showNormalTxt("Error!!", msg) {}
    }


}