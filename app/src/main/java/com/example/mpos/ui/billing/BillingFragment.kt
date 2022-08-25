package com.example.mpos.ui.billing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mpos.R
import com.example.mpos.databinding.BillingFragmentLayoutBinding
import com.example.mpos.ui.cost.viewmodel.CostDashBoardViewModel
import com.example.mpos.utils.*

class BillingFragment : Fragment(R.layout.billing_fragment_layout) {
    private lateinit var binding: BillingFragmentLayoutBinding
    private val viewModel: CostDashBoardViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.semi_white_color_two)
        binding = BillingFragmentLayoutBinding.bind(view)
        viewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { msg ->
                showErrorDialog(msg)
            }
        }
        getConfirmBillingResponse()
        getSendBillToEdcResponse()
        binding.confirmOrderBtn.setOnClickListener {

        }

    }

    private fun getSendBillToEdcResponse() {
        viewModel.sendBillingToEdc.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    hidePb()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { msg ->
                            showErrorDialog(msg)
                        }
                    } else {
                        showErrorDialog("${it.data}")
                    }
                }
                is ApisResponse.Loading -> showPb("${it.data}")
                is ApisResponse.Success -> {
                    hidePb()
                    activity?.msg("Success")
                }
            }
        }
    }

    private fun getConfirmBillingResponse() {
        viewModel.confirmBillingResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    hidePb()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { msg ->
                            showErrorDialog(msg)
                        }
                    } else {
                        showErrorDialog("${it.data}")
                    }
                }
                is ApisResponse.Loading -> showPb("${it.data}")
                is ApisResponse.Success -> {
                    hidePb()
                    activity?.msg("Success Confirm Bill")
                }
            }
        }
    }

    private fun showErrorDialog(msg: String) {
        showDialogBox("Failed", msg, icon = R.drawable.ic_error) {}
    }

    private fun showPb(msg: String) {
        binding.pbLayout.root.show()
        binding.pbLayout.titleTxt.text = msg
    }

    private fun hidePb() {
        binding.pbLayout.root.hide()
    }

}
