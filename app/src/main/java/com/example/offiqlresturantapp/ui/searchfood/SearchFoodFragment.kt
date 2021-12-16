package com.example.offiqlresturantapp.ui.searchfood

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.SearchFoodItemLayoutBinding
import com.example.offiqlresturantapp.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItem
import com.example.offiqlresturantapp.utils.TAG
import com.example.offiqlresturantapp.utils.changeStatusBarColor
import com.example.offiqlresturantapp.utils.invisible
import com.example.offiqlresturantapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFoodFragment : Fragment(R.layout.search_food_item_layout) {
    private lateinit var binding: SearchFoodItemLayoutBinding
    private lateinit var listOfFoodItemToSearchAdaptor: ListOfFoodItemToSearchAdaptor

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color)
        binding = SearchFoodItemLayoutBinding.bind(view)
        binding.backArrowImg.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.crossBtnImg.setOnClickListener {
            binding.searchBoxEd.text = null
        }
        binding.searchBoxEd.doOnTextChanged { text, _, _, _ ->
            Log.i(TAG, "onViewCreated: $text \n ${text?.length}")
            if (text != null && text.isNotEmpty()) {
                binding.crossBtnImg.visible()
            } else {
                binding.crossBtnImg.invisible()
            }
        }


        setRecycleView()
        setData()

    }

    private fun setData() {
        val list = listOf(
            FoodItem(
                foodName = "Chloe Bhature",
                foodPrice = 135,
                foodOffer = "+1 Pepsi Can @15/-",
                offerDesc = "Add  a 200 ml Pepsi Can for 15/-"
            ), FoodItem(
                foodName = "Chloe Samosa Chart",
                foodPrice = 135,
                foodOffer = "By One Get 2 Free",
                offerDesc = "By One Plate get 2 Plate Free"
            ), FoodItem(
                foodName = "Chloe Kulcha",
                foodPrice = 135,
                foodOffer = "Combo Offer",
                offerDesc = "By One Plate get 2 Pepsi Can Free"
            ), FoodItem(
                foodName = "Chloe (Bowl)",
                foodPrice = 135,
                foodOffer = null,
                offerDesc = null
            )
        )

        listOfFoodItemToSearchAdaptor.submitList(list)
    }

    private fun setRecycleView() {
        binding.listOfFoodItem.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            listOfFoodItemToSearchAdaptor = ListOfFoodItemToSearchAdaptor {
                Log.i(TAG, "setRecycleView: $it")
            }
            adapter = listOfFoodItemToSearchAdaptor
        }
    }
}