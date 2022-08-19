package com.example.mpos.ui.searchfood

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mpos.R
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.SearchFoodItemLayoutBinding
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.ui.searchfood.view_model.SearchFoodViewModel
import com.example.mpos.utils.*
import com.google.android.material.snackbar.Snackbar


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

    override fun onResume() {
        super.onResume()
        DealsStoreInstance.getInstance().clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let { flag = it.getBoolean("flag") }
        requireActivity().changeStatusBarColor(R.color.semi_white_color_two)
        binding = SearchFoodItemLayoutBinding.bind(view)
        viewModel.fetchResponseApi()
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
            DealsStoreInstance.getInstance().setResetButtonClick(false)
            listOfFoodItem.addAll(args.list.foodList)
            val action = args.tbl?.let {
                SearchFoodFragmentDirections.actionSearchFoodFragmentToConfirmOderFragment(
                    FoodItemList(listOfFoodItem), it, args.confirmreq
                )
            } ?: SearchFoodFragmentDirections.actionSearchFoodFragmentToCostDashBoardFragment(
                FoodItemList(listOfFoodItem),
                args.confirmreq
            )
            findNavController().navigate(action)
        } else if (flag) {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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
                    val res = it.data as List<*>?
                    if (res.isNullOrEmpty()) {
                        hideOrShow("Loading Menu..")
                    } else {
                        displayData(it.data)
                    }

                }
                is ApisResponse.Success -> {
                    hideOrShow(null)
                    displayData(it.data)
                }
            }
        }

    }

    @Suppress("UNCHECKED_CAST")
    private fun displayData(data: Any?) {
        data?.let { item ->
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