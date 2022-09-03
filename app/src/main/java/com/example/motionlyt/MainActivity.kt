package com.example.motionlyt

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.motionlyt.databinding.ActivityMainBinding
import com.example.motionlyt.datastore.NotesSharedPreference
import com.example.motionlyt.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val notesSharedPreference by lazy {
        NotesSharedPreference.getInstance(this)
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
                    val reg = notesSharedPreference.getReg()
                    val uni = notesSharedPreference.getUniName()
                    setLogCat("DB","reg is $reg")
                    setLogCat("DB","uni is $uni")
                    if (reg == null || uni == null || checkInputValue(reg) || checkInputValue(uni)) {
                        gotToAppFeature()
                    } else {
                       gotNoteFeature()
                    }
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


    private fun gotNoteFeature() {
        startActivity(Intent(this, NoteActivity::class.java))
        this@MainActivity.finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        this.hide()
        binding.pbLayout.hide()
        this.changeStatusBarColor()
    }
}