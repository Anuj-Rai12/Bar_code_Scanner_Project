package com.example.motionlyt.ui.share

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motionlyt.R
import com.example.motionlyt.adaptor.viewpager.ViewPagerAdapter
import com.example.motionlyt.databinding.ShareLayoutFragmentBinding
import com.example.motionlyt.model.data.FileData
import com.example.motionlyt.model.data.FileDataClass
import com.example.motionlyt.ui.share.tabs.HallOfInstitute
import com.example.motionlyt.ui.share.tabs.MyFileFragment
import com.example.motionlyt.ui.share.tabs.SharedFragment
import com.google.android.material.tabs.TabLayoutMediator

class SharingFragment : Fragment(R.layout.share_layout_fragment) {
    private lateinit var binding: ShareLayoutFragmentBinding

    private lateinit var viewPagerAdaptor: ViewPagerAdapter

    private val tabArr = arrayListOf("My Files", "Shared Files", "Hall Of Institution")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ShareLayoutFragmentBinding.bind(view)
        setAdaptor()
        viewPagerAdaptor.setFragment(MyFileFragment())
        viewPagerAdaptor.setFragment(SharedFragment())
        viewPagerAdaptor.setFragment(HallOfInstitute())

        TabLayoutMediator(
            binding.tabs,
            binding.viewPager
        ) { o, p ->
            o.text = tabArr[p]
        }.attach()

    }


     fun getUser(fileData: FileData) {
        val obj = FileDataClass()
        obj.date = fileData.date
        obj.fileName = fileData.fileName
        obj.type = fileData.type
        obj.size = fileData.size
        obj.fileUrl = fileData.fileUrl

        val intent = Intent(requireActivity(), ShareWithFriendsActivity::class.java)
        intent.putExtra("user", obj)
        startActivity(intent)

    }

    private fun setAdaptor() {
        viewPagerAdaptor = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdaptor
    }


}