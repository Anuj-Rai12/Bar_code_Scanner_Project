package com.example.offiqlresturantapp.ui.splash

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
import androidx.navigation.fragment.findNavController
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.data.login.model.api.json.ApkLoginJsonResponse
import com.example.offiqlresturantapp.databinding.SplashSrcLayoutBinding
import com.example.offiqlresturantapp.ui.testingconnection.viewModel.TestingConnectionViewModel
import com.example.offiqlresturantapp.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment(R.layout.splash_src_layout) {
    private lateinit var binding: SplashSrcLayoutBinding
    private val viewModel: TestingConnectionViewModel by viewModels()
    private val animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(), R.anim.bounc_animation)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SplashSrcLayoutBinding.bind(view)
        binding.logoFileId3.animation = animation

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
                doAuthTask()
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
                        if (!checkFieldValue(exp))
                            requireActivity().msg(exp)
                    }
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
            val action = if (string == null) {
                SplashScreenFragmentDirections.actionSplashScreenFragmentToTestingConnectionFragment(
                    null
                )
            } else {
                SplashScreenFragmentDirections.actionSplashScreenFragmentToTableManagementOrCostEstimate(
                    string
                )
            }
            findNavController().navigate(action)
        }
    }

}