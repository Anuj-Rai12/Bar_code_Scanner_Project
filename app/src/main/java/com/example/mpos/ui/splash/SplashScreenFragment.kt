package com.example.mpos.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mpos.R
import com.example.mpos.data.login.model.api.json.ApkLoginJsonResponse
import com.example.mpos.databinding.SplashSrcLayoutBinding
import com.example.mpos.ui.testingconnection.viewModel.TestingConnectionViewModel
import com.example.mpos.utils.*
import com.google.android.material.snackbar.Snackbar

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment(R.layout.splash_src_layout) {
    private lateinit var binding: SplashSrcLayoutBinding
    private val viewModel: TestingConnectionViewModel by viewModels()
    private val animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(), R.anim.bounc_animation)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.light_blue_bg)
        binding = SplashSrcLayoutBinding.bind(view)
        binding.logoFileId3.animation = animation
        doAuthTask()
        viewModel.events.observe(viewLifecycleOwner) {
            var count = 0
            it.getContentIfNotHandled()?.let { str ->
                binding.root.showSandbar(
                    str,
                    Snackbar.LENGTH_INDEFINITE,
                    requireActivity().getColorInt(R.color.color_red)
                ) {
                    if (count > 0) {
                        nextFrag(null)
                    }
                    count++
                    return@showSandbar "OK"
                }
            }
        }
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                Log.i(TAG, "onAnimationStart: Start")
            }

            override fun onAnimationEnd(animation: Animation?) {
                viewModel.doLoginProcess()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                Log.i(TAG, "onAnimationStart: Repeat")
            }

        })

    }

    private fun doAuthTask() {
        viewModel.apk.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    it.exception?.localizedMessage?.let { exp ->
                        Log.i(TAG, "doAuthTask: $exp")
                    }
                    if (it.data == null) {
                        nextFrag(null)
                    } else {
                        nextFrag("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    Log.i(TAG, "checkApiResponse: ${it.data}")
                }
                is ApisResponse.Success -> {
                    it.data?.let { type ->
                        val json = type as ApkLoginJsonResponse
                        if (json.status)
                            nextFrag(json.storeName,json)
                    }
                }
            }
        }
    }


    private fun nextFrag(string: String?,data:ApkLoginJsonResponse?=null) {
        lifecycleScope.launchWhenStarted {
            val action = when (string) {
                null -> {
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginScreenFragment()
                }
                "1" -> {
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToTestingConnectionFragment(
                        null
                    )
                }
                else -> {
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToTableManagementOrCostEstimate(data!!)
                }
            }
            findNavController().safeNavigate(action)
        }
    }

}