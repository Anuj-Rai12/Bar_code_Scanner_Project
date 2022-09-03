package com.example.motionlyt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.motionlyt.adaptor.viewpager.ViewPagerAdapter
import com.example.motionlyt.databinding.AppFeatureActivityBinding
import com.example.motionlyt.ui.splash.AppFeatureDescription
import com.example.motionlyt.utils.changeStatusBarColor
import com.example.motionlyt.utils.hide
import com.example.motionlyt.utils.showSnackBarMsg
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            gotToAppFeature(LoginType.LOGIN.name)
        }

        binding.skipBtn.setOnClickListener {
            gotToAppFeature(LoginType.SIGNIN.name)
        }

        movingOnScreenToAnother()
    }

    private fun movingOnScreenToAnother() {
        lifecycleScope.launch {
            var pos = 0
            while (true) {
                delay(3000)
                pos = (pos % 3)
                binding.viewPagerMainLogin.currentItem = pos
                pos++
            }
        }
    }

    private fun gotToAppFeature(str:String) {
        val intent=Intent(this, LoginActivity::class.java)
        intent.putExtra(intentKey,str)
        startActivity(intent)
    }


    private fun setAdaptor() {
        viewPagerAdaptor = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPagerMainLogin.adapter = viewPagerAdaptor
    }


    companion object {
        const val intentKey = "AppFeaturedActivity"

        enum class LoginType {
            SIGNIN,
            LOGIN
        }
    }

    override fun onResume() {
        super.onResume()
        this.hide()
        this.changeStatusBarColor()
    }
}

