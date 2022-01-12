package com.example.offiqlresturantapp.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.databinding.SplashSrcLayoutBinding
import com.example.offiqlresturantapp.ui.testingconnection.model.api.ApKLoginPost
import com.example.offiqlresturantapp.ui.testingconnection.model.api.json.ApkLoginJsonResponse
import com.example.offiqlresturantapp.ui.testingconnection.viewModel.TestingConnectionViewModel
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.TAG
import com.example.offiqlresturantapp.utils.checkFieldValue
import com.example.offiqlresturantapp.utils.msg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment(R.layout.splash_src_layout) {
    private lateinit var binding: SplashSrcLayoutBinding
    private val viewModel: TestingConnectionViewModel by viewModels()
    private val animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(), R.anim.bounc_animation)
    }

    @Inject
    lateinit var userSoredData: UserSoredData
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SplashSrcLayoutBinding.bind(view)
        binding.logoFileId3.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                Log.i(TAG, "onAnimationStart: strart")
            }

            override fun onAnimationEnd(animation: Animation?) {
                doAuthTask()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                Log.i(TAG, "onAnimationStart: strart")
            }

        })

    }

    private fun doAuthTask() {
        userSoredData.read.asLiveData().observe(viewLifecycleOwner) {
            if (it != null && !checkFieldValue(it.userID!!)) {
                val data = ApKLoginPost(apK = it)
                checkApiResponse(data)
            } else {
                nextFrag(null)
            }
        }
    }

    private fun checkApiResponse(data: ApKLoginPost) {
        viewModel.getApkLoginResponse(data, true).observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    requireActivity().msg("${it.exception?.localizedMessage}")
                    nextFrag(null)
                }
                is ApisResponse.Loading -> {
                    Log.i(TAG, "checkApiResponse: ${it.data}")
                }
                is ApisResponse.Success -> {
                    it.data?.let { type ->
                        val json = type as ApkLoginJsonResponse
                        if (json.status)
                            nextFrag(json.storeName)
                    }
                }
            }
        }
    }

    private fun nextFrag(string: String?) {
        lifecycleScope.launchWhenStarted {
            delay(1000)
            val action = if (string == null) {
                SplashScreenFragmentDirections.actionSplashScreenFragmentToTestingConnectionFragment(null)
            } else {
                SplashScreenFragmentDirections.actionSplashScreenFragmentToTableManagementOrCostEstimate(
                    string
                )
            }
            findNavController().navigate(action)
        }
    }

}