package com.example.motionlyt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.motionlyt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if (binding.root.currentState == R.id.start)
            binding.root.transitionToEnd()
        else
            super.onBackPressed()
    }
}