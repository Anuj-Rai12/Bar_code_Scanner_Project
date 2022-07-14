package com.fbts.mpos.ui.splash

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fbts.mpos.R
import com.fbts.mpos.data.login.model.api.json.ApkLoginJsonResponse
import com.fbts.mpos.databinding.SplashSrcLayoutBinding
import com.fbts.mpos.ui.testingconnection.viewModel.TestingConnectionViewModel
import com.fbts.mpos.utils.*
import com.google.android.material.snackbar.Snackbar

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment(R.layout.splash_src_layout) {
    private lateinit var binding: SplashSrcLayoutBinding
    private val viewModel: TestingConnectionViewModel by viewModels()
    private val animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(), R.anim.bounc_animation)
    }


    @RequiresApi(Build.VERSION_CODES.M)
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
                            nextFrag(json.storeName)
                    }
                }
            }
        }
    }


    private fun nextFrag(string: String?) {
        lifecycleScope.launchWhenStarted {
            throw ExceptionInInitializerError("Next File Error!!")
            /*val action = when (string) {
                null -> {
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginScreenFragment()
                }
                "1" -> {
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToTestingConnectionFragment(
                        null
                    )
                }
                else -> {
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToTableManagementOrCostEstimate(
                        string
                    )
                }
            }
            findNavController().navigate(action)

             */
        }
    }

}