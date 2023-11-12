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
import com.example.mpos.BuildConfig
import com.example.mpos.R
import com.example.mpos.data.login.model.api.json.ApkLoginJsonResponse
import com.example.mpos.data.login.model.api.json.ScreenList
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


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.light_blue_bg)
        binding = SplashSrcLayoutBinding.bind(view)
        binding.versionCode.text = "v${BuildConfig.VERSION_NAME}"
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
                        val jsonTest = ApkLoginJsonResponse(
                            itemScanWithBarcode = false,
                            message = "Login Success",
                            screenList = listOf(
                                ScreenList(
                                    dynamicMenuEnable = false,
                                    billingFromEDC = true,
                                    screenCaption = "0000",
                                    modernSearch = true,
                                    uPICode = "",
                                    estimatePrint = false,
                                    enableCustDetail = false,
                                    screenList = "Billing",
                                    kotPrintFromEDC = true,
                                    paymentLs = listOf("Cash", "Card", "UPI"),
                                    estimatePrintCount = 0
                                ),
                                ScreenList(
                                    dynamicMenuEnable = false,
                                    billingFromEDC = true,
                                    screenCaption = "0000",
                                    modernSearch = true,
                                    uPICode = "",
                                    enableCustDetail = false,
                                    screenList = "SHOWROOMBILLING",
                                    kotPrintFromEDC = true,
                                    estimatePrintCount = 0,
                                    estimatePrint = false,
                                    paymentLs = listOf("Cash", "Card", "UPI")
                                ),
                                ScreenList(
                                    dynamicMenuEnable = false,
                                    billingFromEDC = true,
                                    estimatePrint = false,
                                    screenCaption = "0000",
                                    modernSearch = true,
                                    uPICode = "",
                                    enableCustDetail = false,
                                    screenList = "RESTAURANTBILLING",
                                    kotPrintFromEDC = true,
                                    paymentLs = listOf("Cash", "Card", "UPI"),
                                    estimatePrintCount = 0
                                )
                            ), status = true, storeName = "OAM Industries Ajni"
                        )
                        createLogStatement("LOGIN", "$json")
                        if (json.status) {
                            nextFrag(json.storeName, json)
                        }
                    }
                }
            }
        }
    }


    private fun nextFrag(string: String?, data: ApkLoginJsonResponse? = null) {
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
                    null
                }
            }
            action?.let {
                findNavController().safeNavigate(action)
            }
            if (action == null) {
                val item = Bundle()
                item.putParcelable("TBL_VALUE", data!!)
                findNavController().navigate(
                    R.id.action_splashScreenFragment_to_tableManagementOrCostEstimate,
                    item
                )
            }

        }
    }

}