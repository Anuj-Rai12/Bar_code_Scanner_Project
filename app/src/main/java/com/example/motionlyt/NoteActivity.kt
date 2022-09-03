package com.example.motionlyt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.motionlyt.databinding.NotesActivityBinding
import com.example.motionlyt.utils.changeStatusBarColor
import com.example.motionlyt.utils.hide

class NoteActivity:AppCompatActivity() {
    private val binding by lazy {
        NotesActivityBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }



    override fun onBackPressed() {
        super.onBackPressed()
    }
    override fun onResume() {
        super.onResume()
        this.hide()
        this.changeStatusBarColor(R.color.semi_white_color_two)
    }
}