package com.example.offiqlresturantapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.offiqlresturantapp.databinding.CameraActivityLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {
    private lateinit var binding: CameraActivityLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CameraActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}