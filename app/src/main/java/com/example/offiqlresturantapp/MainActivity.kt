package com.example.offiqlresturantapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.offiqlresturantapp.databinding.ActivityMainBinding
import com.example.offiqlresturantapp.ui.VideoActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.hide()
        this.changeStatusBarColor()
        if (!this.allPermissionsGranted()) {
            this.getRuntimePermissions()
        }

        binding.cameraRecorder.setOnClickListener {
            gotoNextScreen<VideoActivity>(CAMERA)
        }
        binding.videoRecorder.setOnClickListener {
            gotoNextScreen<VideoActivity>(VIDEO)
        }
    }

    private inline fun <reified T> gotoNextScreen(type: String) {
        if (allPermissionsGranted()) {
            val intend = Intent(this, T::class.java)
            intend.putExtra("Type", type)
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