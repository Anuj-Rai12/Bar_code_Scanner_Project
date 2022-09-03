package com.example.motionlyt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.motionlyt.adaptor.viewpager.ViewPagerAdapter
import com.example.motionlyt.databinding.AppFeatureActivityBinding
import com.example.motionlyt.ui.splash.AppFeatureDescription
import com.example.motionlyt.utils.changeStatusBarColor
import com.example.motionlyt.utils.hide
import com.example.motionlyt.utils.showSnackBarMsg
import com.google.android.material.tabs.TabLayoutMediator

class AppFeatureActivity : AppCompatActivity() {
    private val binding by lazy {
        AppFeatureActivityBinding.inflate(layoutInflater)
    }

    private lateinit var viewPagerAdaptor: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setAdaptor()
        viewPagerAdaptor.setFragment(
            AppFeatureDescription(
                R.drawable.ic_notes,
                "Share your app",
                "Share your file in any text\n testing appliction"
            )
        )
        viewPagerAdaptor.setFragment(
            AppFeatureDescription(
                R.drawable.ic_notes,
                "Share your app",
                "Share your file in any text"
            )
        )
        viewPagerAdaptor.setFragment(
            AppFeatureDescription(
                R.drawable.ic_notes,
                "Share your app",
                "Share your file in any text"
            )
        )
        TabLayoutMediator(
            binding.tabItemForIntroScreen,
            binding.viewPagerMainLogin
        ) { _, _ -> }.attach()


        binding.goToNextScr.setOnClickListener {
            binding.viewPagerMainLogin.currentItem =
                (binding.viewPagerMainLogin.currentItem + 1) % 3
        }

        binding.skipBtn.setOnClickListener {
            binding.root.showSnackBarMsg("Ok working...")
        }
    }

    private fun setAdaptor() {
        viewPagerAdaptor = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPagerMainLogin.adapter = viewPagerAdaptor
    }

    override fun onResume() {
        super.onResume()
        this.hide()
        this.changeStatusBarColor()
    }
}

