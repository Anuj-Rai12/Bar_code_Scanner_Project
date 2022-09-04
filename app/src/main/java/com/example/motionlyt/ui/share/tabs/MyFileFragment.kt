package com.example.motionlyt.ui.share.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.databinding.MyFileFragmentLayoutBinding

class MyFileFragment : Fragment(R.layout.my_file_fragment_layout) {

    private lateinit var binding: MyFileFragmentLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MyFileFragmentLayoutBinding.bind(view)

    }
}