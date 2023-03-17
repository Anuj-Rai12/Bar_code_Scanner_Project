package com.example.mpos

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.BillingFragmentLayoutBinding
import com.example.mpos.databinding.TestingLayoutBinding
import com.example.mpos.ui.searchfood.view_model.SearchFoodViewModel
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.msg

class TestingActivity : AppCompatActivity() {

    private val binding by lazy {
        BillingFragmentLayoutBinding.inflate(layoutInflater)
    }

    private val viewModel: SearchFoodViewModel by viewModels()
    private lateinit var foodAdaptor: FoodAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.menuSearchEd.doOnTextChanged { txt, _, _, _ ->
            if (!txt.isNullOrEmpty()) {
                viewModel.searchQuery("%$txt%")
            }else{
                foodAdaptor.submitList(listOf())
            }
        }
        setAdaptor()
        setDataInfo()
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
                    val ls=it.data as List<ItemMaster>?
                    foodAdaptor.submitList(ls)
                }
            }
        }
    }


}