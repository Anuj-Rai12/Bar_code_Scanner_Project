package com.example.motionlyt.ui.share

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.motionlyt.R
import com.example.motionlyt.databinding.ShareWithFriendsLayoutBinding
import com.example.motionlyt.model.data.FileDataClass
import com.example.motionlyt.utils.changeStatusBarColor
import com.example.motionlyt.utils.hide
import com.example.motionlyt.utils.toastMsg

class ShareWithFriendsActivity : AppCompatActivity() {

    private val binding by lazy {
        ShareWithFriendsLayoutBinding.inflate(layoutInflater)
    }

    private val extras by lazy {
        intent.getSerializableExtra("user") as FileDataClass?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        toastMsg("$extras")
    }

    override fun onResume() {
        super.onResume()
        this.hide()
        this.changeStatusBarColor(R.color.semi_white_color_two)
    }
}