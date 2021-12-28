package com.example.offiqlresturantapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.offiqlresturantapp.databinding.ActivityMainBinding
import com.example.offiqlresturantapp.model.test.apklogin.APKLogin
import com.example.offiqlresturantapp.model.test.apklogin.EnvelopePostItem
import com.example.offiqlresturantapp.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val itemPost = EnvelopePostItem(
            apk = APKLogin(
                "RO404",
                "Test",
                "123"
            )
        )

        viewModel.getResult(itemPost).observe(this) {
            Log.i(TAG, "onCreate: $it")
            binding.tvId.text = "$it"
        }
    }
}