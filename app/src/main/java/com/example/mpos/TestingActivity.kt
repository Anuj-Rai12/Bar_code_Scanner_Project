package com.example.mpos

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.mpos.data.feedback.feedbck.FeedBackRequest
import com.example.mpos.data.feedback.feedbck.FinalFeedbackSendBody
import com.example.mpos.data.feedback.invoice.FinalInvoiceSendBody
import com.example.mpos.data.feedback.invoice.FinalInvoiceSendRequest
import com.example.mpos.data.feedback.invoice.ResponseFinalInvoiceSend
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.BillingFragmentLayoutBinding
import com.example.mpos.payment.viewmodel.FeedBackViewModel
import com.example.mpos.ui.searchfood.view_model.SearchFoodViewModel
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.createLogStatement
import com.example.mpos.utils.msg

class TestingActivity : AppCompatActivity() {

    private val binding by lazy {
        BillingFragmentLayoutBinding.inflate(layoutInflater)
    }
    private val feedBackViewModel: FeedBackViewModel by viewModels()
    private val viewModel: SearchFoodViewModel by viewModels()
    private lateinit var foodAdaptor: FoodAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.menuSearchEd.doOnTextChanged { txt, _, _, _ ->
            if (!txt.isNullOrEmpty()) {
                viewModel.searchQuery("%$txt%")
            } else {
                foodAdaptor.submitList(listOf())
            }
        }
        setAdaptor()
        setDataInfo()
        getFeedBackResponse()

        binding.confirmOrderBtn.setOnClickListener {
            feedBackViewModel.getFeedBack(FinalInvoiceSendRequest(FinalInvoiceSendBody("${true}")))
        }
    }

    private fun setAdaptor() {
        binding.menuRecycle.apply {
            foodAdaptor = FoodAdaptor {
                msg("$it")
            }
            adapter = foodAdaptor
        }
    }

    private fun setDataInfo() {
        viewModel.fdInfo.observe(this) {
            when (it) {
                is ApisResponse.Error -> {
                    //hideOrShow(null)
                    it.exception?.localizedMessage?.let { e ->
                        //showSnackBar(e, R.color.color_red, Snackbar.LENGTH_INDEFINITE)
                        msg(e)
                    }
                }
                is ApisResponse.Loading -> {}
                is ApisResponse.Success -> {
                    foodAdaptor.notifyDataSetChanged()
                    val ls = it.data as List<ItemMaster>?
                    foodAdaptor.submitList(ls)
                }
            }
        }
    }

    private fun getFeedBackResponse() {
        feedBackViewModel.isFeedBackSend.observe(this) { res ->
            res?.let {
                when (it) {
                    is ApisResponse.Error -> {
                        createLogStatement(
                            "Feed_Testing",
                            "${it.data} and ${it.exception?.localizedMessage}"
                        )
                    }
                    is ApisResponse.Loading -> {
                        createLogStatement("Feed_Testing", "${it.data}")
                    }
                    is ApisResponse.Success -> {
                        createLogStatement(
                            "Feed_Testing",
                            "${it.data}"
                        )
                        if (it.data is ResponseFinalInvoiceSend) {
                            feedBackViewModel.getFeedBack(FeedBackRequest(FinalFeedbackSendBody("${true}")))
                        }
                    }
                }
            }
        }
    }

}