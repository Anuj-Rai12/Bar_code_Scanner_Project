package com.example.offiqlresturantapp.ui.searchfood

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.SearchFoodItemLayoutBinding
import com.example.offiqlresturantapp.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItem
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItemList
import com.example.offiqlresturantapp.ui.searchfood.model.OfferDesc
import com.example.offiqlresturantapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFoodFragment : Fragment(R.layout.search_food_item_layout) {
    private lateinit var binding: SearchFoodItemLayoutBinding
    private lateinit var listOfFoodItemToSearchAdaptor: ListOfFoodItemToSearchAdaptor
    private var listOfFoodItem = mutableListOf<FoodItem>()
    private var flag: Boolean = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("flag", flag)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let { flag = it.getBoolean("flag") }
        requireActivity().changeStatusBarColor(R.color.semi_white_color)
        binding = SearchFoodItemLayoutBinding.bind(view)
        onBackPress()
        binding.backArrowImg.setOnClickListener {
            chooseOptionBackScreenOption()
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

    private fun onBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            chooseOptionBackScreenOption()
        }.handleOnBackPressed()
    }

    private fun chooseOptionBackScreenOption() {
        Log.i(TAG, "chooseOptionBackScreenOption: $listOfFoodItem\n\n And Flag Value is -> $flag")
        if (flag && !listOfFoodItem.isNullOrEmpty()) {
            val action = SearchFoodFragmentDirections.actionSearchFoodFragmentToConfirmOderFragment(
                FoodItemList(listOfFoodItem)
            )
            findNavController().navigate(action)
        } else if (flag) {
            findNavController().popBackStack()
        }
    }

    private fun setData() {
        val list = listOf(
            FoodItem(
                foodName = "Chloe Bhature",
                foodPrice = 135,
                foodAmt = 135,
                foodOffer = "+1 Pepsi Can @15/-",
                offerDesc = listOf(
                    OfferDesc(
                        price = 15,
                        "Add  a 200 ml Pepsi Can for 15/-",
                    ),
                    OfferDesc(
                        price = 10,
                        "Add a 10 ml Sugar for 10/-",
                    )
                )
            ), FoodItem(
                foodName = "Chloe Samosa Chart",
                foodPrice = 135,
                foodAmt = 135,
                foodOffer = "By One Get 2 Free",
                offerDesc = listOf(
                    OfferDesc(
                        price = 20,
                        "By One Plate get 2 Plate Free"
                    )
                )
            ), FoodItem(
                foodName = "Chloe Kulcha",
                foodPrice = 135,
                foodAmt = 135,
                foodOffer = "Combo Offer",
                offerDesc = listOf(
                    OfferDesc(
                        price = 12,
                        "By One Plate get 2 Pepsi Can Free"
                    )
                )
            ), FoodItem(
                foodName = "Chloe (Bowl)",
                foodPrice = 135,
                foodAmt = 135,
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
                activity?.msg("$it")
                if (listOfFoodItem.contains(it)) {
                    listOfFoodItem.remove(it)
                    listOfFoodItem.add(it)
                } else
                    listOfFoodItem.add(it)
            }
            flag = true
            adapter = listOfFoodItemToSearchAdaptor
        }
    }
}