package com.example.offiqlresturantapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.offiqlresturantapp.databinding.ActivityMainBinding
import com.example.offiqlresturantapp.utils.changeStatusBarColor
import com.example.offiqlresturantapp.utils.hide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.hide()
        this.changeStatusBarColor()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}