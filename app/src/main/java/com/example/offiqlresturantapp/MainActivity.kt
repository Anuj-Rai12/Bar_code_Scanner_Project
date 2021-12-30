package com.example.offiqlresturantapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.offiqlresturantapp.databinding.ActivityMainBinding
import com.example.offiqlresturantapp.ui.CameraActivity
import com.example.offiqlresturantapp.ui.VideoActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!this.allPermissionsGranted()) {
            this.getRuntimePermissions()
        }

        binding.cameraRecorder.setOnClickListener {
            gotoNextScreen<CameraActivity>()
        }
        binding.videoRecorder.setOnClickListener {
            gotoNextScreen<VideoActivity>()
        }
    }

    private inline fun <reified T> gotoNextScreen() {
        if (allPermissionsGranted()) {
            val intend = Intent(this, T::class.java)
            startActivity(intend)
            finish()
        } else
            return
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (this.allPermissionsGranted()) {
            Log.i(TAG, "onRequestPermissionsResult: All Permission Granted")
        }
    }

}