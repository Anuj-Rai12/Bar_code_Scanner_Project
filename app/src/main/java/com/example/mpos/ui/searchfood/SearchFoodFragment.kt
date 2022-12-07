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
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingJsonResponse
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.SearchFoodItemLayoutBinding
import com.example.mpos.ui.crosselling.CrossSellingDialog
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.ui.searchfood.view_model.SearchFoodViewModel
import com.example.mpos.utils.*
import com.google.android.material.snackbar.Snackbar


class SearchFoodFragment : Fragment(R.layout.search_food_item_layout), OnBottomSheetClickListener {
    private lateinit var binding: SearchFoodItemLayoutBinding
    private lateinit var listOfFoodItemToSearchAdaptor: ListOfFoodItemToSearchAdaptor
    private var listOfFoodItem = mutableListOf<ItemMasterFoodItem>()
    private var flag: Boolean = false
    private val viewModel: SearchFoodViewModel by viewModels()
    private val args: SearchFoodFragmentArgs by navArgs()

    private var crossSellingItemMaster: ItemMasterFoodItem? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("flag", flag)
        outState.putString("REST_INS",RestaurantSingletonCls.getInstance().getScreenType())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let { flag = it.getBoolean("flag")
            it.getString("REST_INS")?.let {type-> RestaurantSingletonCls.getInstance().setScreenType(type) }
        }
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
        getCrossSellingResponse()
        setRecycleView()
        setData()

    }

    private fun getCrossSellingResponse() {
        viewModel.crossSellingResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    binding.pbLayout.root.hide()
                    if (it.data != null) {
                        showDialogBox("Failed", "${it.data}", icon = R.drawable.ic_error) {}
                    } else {
                        it.exception?.localizedMessage?.let { exp ->
                            showDialogBox("Failed", exp, icon = R.drawable.ic_error) {}
                        }
                    }
                }
                is ApisResponse.Loading -> {
                    binding.pbLayout.root.show()
                    binding.pbLayout.titleTxt.text = it.data as String
                }
                is ApisResponse.Success -> {
                    binding.pbLayout.root.hide()
                    val res = it.data as CrossSellingJsonResponse
                    openCrossSellingDialog(res)
                }
            }
        }
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
            DealsStoreInstance.getInstance().setIsResetButtonClick(false)
            listOfFoodItem.addAll(args.list.foodList)
            val action = when (WhereToGoFromSearch.valueOf(args.type)) {

                WhereToGoFromSearch.TABLEMANGMENT -> {
                    SearchFoodFragmentDirections.actionSearchFoodFragmentToConfirmOderFragment(
                        FoodItemList(listOfFoodItem), args.tbl!!, args.confirmreq
                    )
                }

                WhereToGoFromSearch.COSTESTIMATE -> {
                    SearchFoodFragmentDirections.actionSearchFoodFragmentToCostDashBoardFragment(
                        FoodItemList(listOfFoodItem),
                        args.confirmreq
                    )
                }

                WhereToGoFromSearch.SHOWROOMESTIMATE -> {
                    SearchFoodFragmentDirections.actionSearchFoodFragmentToShowRoomEstimationFragment(
                        FoodItemList(listOfFoodItem),
                        args.confirmreq
                    )
                }

                WhereToGoFromSearch.RESTAURANTESTIMATE -> {
                    SearchFoodFragmentDirections.actionSearchFoodFragmentToRestaurantEstimationFragments(
                        FoodItemList(listOfFoodItem),
                        args.confirmreq
                    )
                }

                WhereToGoFromSearch.BILLPAYMENT -> {
                    SearchFoodFragmentDirections.actionSearchFoodFragmentToBillingFragment(
                        FoodItemList(listOfFoodItem),
                        args.confirmreq
                    )
                }

                WhereToGoFromSearch.SHOWROOMBILLING -> {
                    SearchFoodFragmentDirections.actionSearchFoodFragmentToShowRoomBillingFragment(
                        FoodItemList(listOfFoodItem),
                        args.confirmreq
                    )
                }

                WhereToGoFromSearch.RESTARURANTBILLING -> {
                    SearchFoodFragmentDirections.actionSearchFoodFragmentToRestaurantBillingFragment(
                        FoodItemList(listOfFoodItem),
                        args.confirmreq
                    )
                }

            }
            findNavController().safeNavigate(action)
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
            listOfFoodItemToSearchAdaptor =
                ListOfFoodItemToSearchAdaptor(itemClickListerForListOfFood = {
                    val msg = if (!checkFieldValue(it.itemMaster.itemName)) it.itemMaster.itemName
                    else it.itemMaster.itemDescription

                    showSnackBar(msg, R.color.green_color, Snackbar.LENGTH_SHORT)
                    val item = listOfFoodItem.find { res -> res.itemMaster.id == it.itemMaster.id }
                    if (item != null) {
                        listOfFoodItem.remove(item)
                    }
                    listOfFoodItem.add(it)

                    Log.i(TAG, "setRecycleView: $listOfFoodItem")
                }, itemClickListerCrossSelling = { itemMaster ->
                    crossSellingItemMaster = itemMaster
                    viewModel.getCrossSellingItem(itemMaster.itemMaster.itemCode)
                })
            flag = true
            adapter = listOfFoodItemToSearchAdaptor
        }
    }


    private fun openCrossSellingDialog(response: CrossSellingJsonResponse) {
        val dialog = CrossSellingDialog(activity!!)
        dialog.itemClicked = this
        dialog.showCrossSellingDialog(response)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> onItemClicked(response: T) {
        val res = response as Pair<Double, CrossSellingJsonResponse>
        crossSellingItemMaster?.let {
            val msg = if (!checkFieldValue(it.itemMaster.itemName)) it.itemMaster.itemName
            else it.itemMaster.itemDescription

            showSnackBar(msg, R.color.green_color, Snackbar.LENGTH_SHORT)
            val item = listOfFoodItem.find { res -> res.itemMaster.id == it.itemMaster.id }
            if (item != null) {
                listOfFoodItem.remove(item)
            }
            listOfFoodItem.add(
                ItemMasterFoodItem(
                    itemMaster = it.itemMaster,
                    foodQty = it.foodQty,
                    foodAmt = it.foodAmt + res.first,
                    bg = listOfBg[2],
                    free_txt = it.free_txt,
                    isDeal = it.isDeal,
                    crossSellingItems = res.second
                )
            )
            crossSellingItemMaster = null
        } ?: activity?.msg("Cannot Add Item")

    }

}