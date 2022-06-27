package com.example.offiqlresturantapp.ui.searchfood

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.data.item_master_sync.json.ItemMaster
import com.example.offiqlresturantapp.databinding.SearchFoodItemLayoutBinding
import com.example.offiqlresturantapp.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItemList
import com.example.offiqlresturantapp.ui.searchfood.model.ItemMasterFoodItem
import com.example.offiqlresturantapp.ui.searchfood.view_model.SearchFoodViewModel
import com.example.offiqlresturantapp.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFoodFragment : Fragment(R.layout.search_food_item_layout) {
    private lateinit var binding: SearchFoodItemLayoutBinding
    private lateinit var listOfFoodItemToSearchAdaptor: ListOfFoodItemToSearchAdaptor
    private var listOfFoodItem = mutableListOf<ItemMasterFoodItem>()
    private var flag: Boolean = false
    private val viewModel: SearchFoodViewModel by viewModels()
    private val args: SearchFoodFragmentArgs by navArgs()

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

        viewModel.events.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { str ->
                showSnackBar(str, color = R.color.color_red, length = Snackbar.LENGTH_INDEFINITE)
            }
        }


        onBackPress()
        binding.backArrowImg.setOnClickListener {
            chooseOptionBackScreenOption()
        }
        binding.crossBtnImg.setOnClickListener {
            if (!binding.searchBoxEd.text.isNullOrEmpty() && !binding.searchBoxEd.text.isNullOrBlank()) {
                binding.searchBoxEd.text = null
                viewModel.getInitialData()
            }
        }
        binding.searchBoxEd.doOnTextChanged { text, _, _, _ ->
            Log.i(TAG, "onViewCreated: $text \n ${text?.length}")
            if (text != null && text.isNotEmpty()) {
                viewModel.searchQuery("%$text%")
            } else {
                viewModel.getInitialData()
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


    private fun hideOrShow(msg: String?) {
        msg?.let {
            binding.pbLayout.root.show()
            binding.pbLayout.titleTxt.text = it
        } ?: binding.pbLayout.root.hide()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun showSnackBar(msg: String, color: Int, length: Int) {
        binding.root.showSandbar(
            msg,
            length,
            requireActivity().getColorInt(color)
        ) {
            return@showSandbar "OK"
        }
    }


    private fun chooseOptionBackScreenOption() {
        Log.i(TAG, "chooseOptionBackScreenOption: $listOfFoodItem\n\n And Flag Value is -> $flag")
        if (flag && listOfFoodItem.isNotEmpty()) {
            listOfFoodItem.addAll(args.list.foodList)
            val action = SearchFoodFragmentDirections.actionSearchFoodFragmentToConfirmOderFragment(
                FoodItemList(listOfFoodItem), args.tbl
            )
            findNavController().navigate(action)
        } else if (flag) {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setData() {
        viewModel.fdInfo.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    hideOrShow(null)
                    it.exception?.localizedMessage?.let { e ->
                        showSnackBar(e, R.color.color_red, Snackbar.LENGTH_INDEFINITE)
                    }
                }
                is ApisResponse.Loading -> {
                    hideOrShow(it.data?.toString())
                }
                is ApisResponse.Success -> {
                    hideOrShow(null)
                    it.data?.let { item ->
                        (item as List<ItemMaster>).let { res ->
                            listOfFoodItemToSearchAdaptor.notifyDataSetChanged()
                            listOfFoodItemToSearchAdaptor.submitList(res)
                            binding.listOfFoodItem.smoothScrollToPosition(0)
                        }
                    } ?: showSnackBar(
                        "No Data Found!!",
                        R.color.color_red,
                        Snackbar.LENGTH_LONG
                    )
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setRecycleView() {
        binding.listOfFoodItem.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            listOfFoodItemToSearchAdaptor = ListOfFoodItemToSearchAdaptor {
                val msg = if (!checkFieldValue(it.itemMaster.itemName)) it.itemMaster.itemName
                else it.itemMaster.itemDescription

                showSnackBar(msg, R.color.green_color, Snackbar.LENGTH_SHORT)
                val item = listOfFoodItem.find { res -> res.itemMaster.id == it.itemMaster.id }
                if (item == null) {
                    listOfFoodItem.add(it)
                } else {
                    listOfFoodItem.remove(item)
                    listOfFoodItem.add(it)
                }
                Log.i(TAG, "setRecycleView: $listOfFoodItem")
                /*if (listOfFoodItem.contains(it)) {
                    listOfFoodItem.remove(it)
                    listOfFoodItem.add(it)
                } else
                    listOfFoodItem.add(it)*/
            }
            flag = true
            adapter = listOfFoodItemToSearchAdaptor
        }
    }
}