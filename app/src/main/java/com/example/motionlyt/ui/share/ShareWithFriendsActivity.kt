package com.example.motionlyt.ui.share

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.motionlyt.R
import com.example.motionlyt.databinding.ShareWithFriendsLayoutBinding
import com.example.motionlyt.model.data.FileDataClass
import com.example.motionlyt.model.userinfo.User
import com.example.motionlyt.ui.share.adaptor.FriendsAdaptor
import com.example.motionlyt.utils.changeStatusBarColor
import com.example.motionlyt.utils.hide
import com.example.motionlyt.utils.toastMsg

class ShareWithFriendsActivity : AppCompatActivity() {

    private val binding by lazy {
        ShareWithFriendsLayoutBinding.inflate(layoutInflater)
    }

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

        setAdaptor()
        toastMsg("${extras?.size}")
        fileDataAdaptor.submitList(
            listOf(
                User("anuj ra", "", joindate = "20/09/2002"),
                User("anuj ra", "", joindate = "20/09/2002"),
                User("anuj ra", "", joindate = "20/09/2002"),
                User("anuj ra", "", joindate = "20/09/2002"),
                User("anuj ra", "", joindate = "20/09/2002"),
                User("anuj ra", "", joindate = "20/09/2002")
            )
        )
        fileDataAdaptor.userList.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.layoutBtn.text = "all"
            } else {
                binding.layoutBtn.text = "${it.size}"
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
}