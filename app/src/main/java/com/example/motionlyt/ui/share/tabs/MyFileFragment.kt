package com.example.motionlyt.ui.share.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.MyFileFragmentLayoutBinding
import com.example.motionlyt.model.data.FileData
import com.example.motionlyt.ui.share.adaptor.FileDataAdaptor

class MyFileFragment : Fragment(R.layout.my_file_fragment_layout) {


    private lateinit var adaptor: FileDataAdaptor
    private lateinit var binding: MyFileFragmentLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MyFileFragmentLayoutBinding.bind(view)
        setRecycle()
        adaptor.submitList(FileData.list)
    }

    private fun setRecycle() {
        binding.recycleView.apply {
            this@MyFileFragment.adaptor=FileDataAdaptor {

            }
            adapter=this@MyFileFragment.adaptor
        }
    }
}