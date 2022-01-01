package com.example.offiqlresturantapp.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.changeStatusBarColor
import com.example.offiqlresturantapp.databinding.CameraActivityLayoutBinding
import com.example.offiqlresturantapp.hide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {
    private lateinit var binding: CameraActivityLayoutBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.hide()
        this.changeStatusBarColor(R.color.black)
        binding = CameraActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}