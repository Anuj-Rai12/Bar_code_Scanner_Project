package com.example.motionlyt

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.motionlyt.databinding.ActivityMainBinding
import com.example.motionlyt.utils.changeStatusBarColor
import com.example.motionlyt.utils.hide
import com.example.motionlyt.utils.show
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.hide()
        this.changeStatusBarColor()

        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                runBlocking {
                    binding.pbLayout.show()
                    delay(2000)
                    gotToAppFeature()
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })

        binding.imageIcon.animation = animation
    }

    private fun gotToAppFeature() {
        startActivity(Intent(this, AppFeatureActivity::class.java))
        this@MainActivity.finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        this.hide()
        binding.pbLayout.hide()
        this.changeStatusBarColor()
    }
}