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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.data.item_master_sync.json.ItemMaster
import com.example.offiqlresturantapp.databinding.SearchFoodItemLayoutBinding
import com.example.offiqlresturantapp.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItemList
import com.example.offiqlresturantapp.ui.searchfood.view_model.SearchFoodViewModel
import com.example.offiqlresturantapp.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFoodFragment : Fragment(R.layout.search_food_item_layout) {
    private lateinit var binding: SearchFoodItemLayoutBinding
    private lateinit var listOfFoodItemToSearchAdaptor: ListOfFoodItemToSearchAdaptor
    private var listOfFoodItem = mutableListOf<ItemMaster>()
    private var flag: Boolean = false
    private val viewModel: SearchFoodViewModel by viewModels()

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
        if (flag && !listOfFoodItem.isNullOrEmpty()) {
            val action = SearchFoodFragmentDirections.actionSearchFoodFragmentToConfirmOderFragment(
                FoodItemList(listOf())
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
                        }
                    } ?: showSnackBar(
                        "UnKnow Error Found!!",
                        R.color.color_red,
                        Snackbar.LENGTH_LONG
                    )
                }
            }
        }

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