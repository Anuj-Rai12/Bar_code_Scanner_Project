package com.example.motionlyt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.motionlyt.databinding.LoginActivityBinding
import com.example.motionlyt.ui.auth.CreateAccountFragment
import com.example.motionlyt.ui.auth.LoginAccountFragment
import com.example.motionlyt.utils.changeStatusBarColor
import com.example.motionlyt.utils.hide

class LoginActivity:AppCompatActivity() {
    private val binding by lazy {
        LoginActivityBinding.inflate(layoutInflater)
    }
    private val extras by lazy {
        intent.extras
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        extras?.let {
            it.getString(AppFeatureActivity.intentKey)?.let {key->
             when(AppFeatureActivity.Companion.LoginType.valueOf(key)){
                 AppFeatureActivity.Companion.LoginType.SIGNIN -> {
                     val fragmentObj = CreateAccountFragment()
                     val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                     transaction.add(R.id.fg_1, fragmentObj)
                     transaction.commit()
                 }
                 AppFeatureActivity.Companion.LoginType.LOGIN -> {
                     val fragmentObj = LoginAccountFragment()
                     val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                     transaction.add(R.id.fg_1, fragmentObj)
                     transaction.commit()
                 }
             }
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        this.hide()
        this.changeStatusBarColor()
    }
}