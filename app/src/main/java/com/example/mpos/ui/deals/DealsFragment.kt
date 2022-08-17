package com.example.mpos.ui.deals

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mpos.R
import com.example.mpos.databinding.DealsFragmentLayoutBinding
import com.example.mpos.ui.deals.viewmodel.DealsViewModel
import com.example.mpos.utils.*

class DealsFragment : Fragment(R.layout.deals_fragment_layout) {

    private lateinit var binding: DealsFragmentLayoutBinding
    private val viewModel: DealsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.semi_white_color_two)
        binding = DealsFragmentLayoutBinding.bind(view)
        viewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { err ->
                showErrorMessage(err)
            }
        }
        getDealsResponse()
        getDealsItemResponse()
        getConfirmDealsResponse()

    }

    private fun getConfirmDealsResponse() {
        viewModel.dealConfirmResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    binding.pbLayout.root.hide()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { err ->
                            showErrorMessage(err)
                        }
                    } else {
                        showErrorMessage("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    binding.pbLayout.titleTxt.text = "${it.data}"
                    binding.pbLayout.root.show()
                }
                is ApisResponse.Success -> {
                    Log.i("DEALS", "getConfirmDealsResponse: ${it.data}")
                    activity?.msg("${it.data}")
                }
            }
        }
    }

    private fun getDealsItemResponse() {
        viewModel.dealItemResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    binding.pbLayout.root.hide()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { err ->
                            showErrorMessage(err)
                        }
                    } else {
                        showErrorMessage("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    binding.pbLayout.titleTxt.text = "${it.data}"
                    binding.pbLayout.root.show()
                }
                is ApisResponse.Success -> {
                    binding.pbLayout.root.hide()
                    Log.i("DEALS", "getDealsItemResponse: ${it.data}")
                    activity?.msg("${it.data}")
                }
            }
        }
    }

    private fun getDealsResponse() {
        viewModel.dealsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    binding.pbLayout.root.hide()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { err ->
                            showErrorMessage(err)
                        }
                    } else {
                        showErrorMessage("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    binding.pbLayout.titleTxt.text = "${it.data}"
                    binding.pbLayout.root.show()
                }
                is ApisResponse.Success -> {
                    binding.pbLayout.root.hide()
                    activity?.msg("${it.data}")
                }
            }
        }
    }


    private fun showErrorMessage(msg: String) {
        showDialogBox("Failed", msg, icon = R.drawable.ic_error) {}
    }

    override fun onResume() {
        super.onResume()
        binding.topAppBar.title = "Deals"
        viewModel.getDeals()
    }
}