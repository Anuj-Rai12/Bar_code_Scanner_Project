package com.example.offiqlresturantapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.offiqlresturantapp.databinding.ActivityMainBinding
import com.example.offiqlresturantapp.model.test.apkJanLogin.APKLoginCls
import com.example.offiqlresturantapp.model.test.apkJanLogin.EnvelopeOption
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
        val itemPost = EnvelopeOption(
            APKLoginCls(
                "RO404",
                "Test",
                "123"
            )
        )
        Log.i(TAG, "onCreate: $itemPost")
        /*viewModel.getResult(itemPost).observe(this) {
            Log.i(TAG, "onCreate: $it")
            binding.tvId.text = "$it"
        }*/

        viewModel.getItemResult().observe(this){
            Log.i(TAG, "onCreate: $it")
            binding.tvId.text = "$it"
        }
    }
}