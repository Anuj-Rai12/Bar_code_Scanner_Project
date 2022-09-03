package com.example.motionlyt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.motionlyt.databinding.NotesActivityBinding
import com.example.motionlyt.datastore.NotesSharedPreference
import com.example.motionlyt.utils.changeStatusBarColor
import com.example.motionlyt.utils.hide
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem

class NoteActivity : AppCompatActivity() {
    private val binding by lazy {
        NotesActivityBinding.inflate(layoutInflater)
    }

    private lateinit var navHostFragment: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHostFragment = navHost.findNavController()
        setUpBottomNav()

    }

    private fun setUpBottomNav() {
        val menuItem = arrayOf(
            CbnMenuItem(R.drawable.ic_share, R.drawable.avd_share, R.id.sharingFragment),
            CbnMenuItem(R.drawable.ic_upload, R.drawable.avd_upload, R.id.uploadFragment),
            CbnMenuItem(
                R.drawable.ic_account,
                R.drawable.avd_account,
                R.id.profileFragment
            )
        )
        binding.bottomNavView.setMenuItems(menuItem, 1)
        binding.bottomNavView.setupWithNavController(navHostFragment)
    }


    fun logout() {
        NotesSharedPreference.getInstance(this).removeAll()
        startActivity(Intent(this, AppFeatureActivity::class.java))
        this@NoteActivity.finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        this.hide()
        this.changeStatusBarColor(R.color.semi_white_color_two)
    }
}