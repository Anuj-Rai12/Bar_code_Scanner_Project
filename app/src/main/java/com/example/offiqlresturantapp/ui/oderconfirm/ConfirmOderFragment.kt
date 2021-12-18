package com.example.offiqlresturantapp.ui.oderconfirm

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.ConfirmOrderLayoutBinding
import com.example.offiqlresturantapp.ui.oderconfirm.adaptor.ConfirmOderFragmentAdaptor
import com.example.offiqlresturantapp.ui.oderconfirm.model.FoodItemSelected
import com.example.offiqlresturantapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmOderFragment : Fragment(R.layout.confirm_order_layout) {
    private lateinit var binding: ConfirmOrderLayoutBinding
    private lateinit var confirmOderFragmentAdaptor: ConfirmOderFragmentAdaptor


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color)
        binding = ConfirmOrderLayoutBinding.bind(view)
        binding.qrCodeScan.setOnClickListener {
            requireActivity().msg(getString(R.string.scan_btn))
        }
        setRecycle()
        setData()
        binding.viewOfferBtn.setOnClickListener {
            binding.orderRecycleViewHint.hide()
            binding.listOfItemRecycleView.show()
        }

        binding.restItemBtn.setOnClickListener {
            binding.orderRecycleViewHint.show()
            binding.listOfItemRecycleView.hide()
        }

        binding.searchBoxTxt.setOnClickListener {
            //New Fragment
            val action =
                ConfirmOderFragmentDirections.actionConfirmOderFragmentToSearchFoodFragment()
            findNavController().navigate(action)
        }
    }

    private fun setData() {
        val list = listOf(
            FoodItemSelected(
                foodName = "Chloe Bhature",
                foodAmt = 125,
                foodRate = 125,
                foodQty = 1,
                bg = R.drawable.food_item_one_selcetion_layout
            ),
            FoodItemSelected(
                foodName = "Chloe Samosa Chart",
                foodAmt = 13,
                foodRate = 13,
                foodQty = 2,
                bg = R.drawable.food_item_two_selection_layout
            ),
            FoodItemSelected(
                foodName = "Chloe Kulcha",
                foodAmt = 15,
                foodRate = 15,
                foodQty = 3,
                bg = R.drawable.food_item_one_selcetion_layout
            ),
            FoodItemSelected(
                foodName = "Chloe (Bowl)",
                foodAmt = 135,
                foodRate = 135,
                foodQty = 4,
                bg = R.drawable.food_item_two_selection_layout
            )

        )

        confirmOderFragmentAdaptor.submitList(list)
    }

    private fun setRecycle() {
        binding.listOfItemRecycleView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            confirmOderFragmentAdaptor = ConfirmOderFragmentAdaptor {
                Log.i(TAG, "setRecycle: $it")
            }
            adapter = confirmOderFragmentAdaptor
        }
    }
}