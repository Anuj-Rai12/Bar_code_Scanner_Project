package com.example.mpos.ui.deals

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mpos.R
import com.example.mpos.data.deals.json.AddOnMenu
import com.example.mpos.data.deals.json.DealsJsonResponse
import com.example.mpos.data.deals.scan_and_find_deals.json.ScanAndFindDealsJson
import com.example.mpos.data.deals.scan_and_find_deals.json.ScanAndFindDealsJsonItem
import com.example.mpos.databinding.DealsFragmentLayoutBinding
import com.example.mpos.ui.deals.adaptor.DealsAdaptor
import com.example.mpos.ui.deals.viewmodel.DealsViewModel
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.utils.*

class DealsFragment : Fragment(R.layout.deals_fragment_layout), OnBottomSheetClickListener {

    private lateinit var binding: DealsFragmentLayoutBinding
    private val viewModel: DealsViewModel by viewModels()
    private var dealsAddOnMenuAdaptor: DealsAdaptor<AddOnMenu>? = null
    private var dealsAddOnMenuItemAdaptor: DealsAdaptor<ScanAndFindDealsJsonItem>? = null
    private var currentIcon = R.drawable.ic_arrow_back_24
    private var currentDeals: AddOnMenu? = null

    private val listOfFoodItem = mutableListOf<ItemMasterFoodItem>()
    private var isEveryThingSetUp: Boolean = false
    private val args: DealsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.semi_white_color_two)
        binding = DealsFragmentLayoutBinding.bind(view)
        viewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { err ->
                showErrorMessage(err)
            }
        }
        getDealsResponse()
        getDealsItemResponse()
        onBackPress()
        binding.topAppBar.setNavigationOnClickListener {
            if (currentIcon == R.drawable.ic_arrow_back_24) {
                onBackPress()
            } else {
                currentDeals?.let {
                    val res = DealsStoreInstance.getInstance().addDealsItem(it)
                    listOfFoodItem.add(res)
                    activity?.msg("${binding.topAppBar.title} ${getEmojiByUnicode(0x2705)}")
                    return@let
                } ?: activity?.msg("${binding.topAppBar.title} Not Added")
                initial()
            }
        }
        binding.swipeLayout.setOnRefreshListener {
            initial()
        }

    }

    private fun onBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            chooseOptionBackScreenOption()
        }.handleOnBackPressed()
    }


    private fun chooseOptionBackScreenOption() {
        Log.i(
            TAG,
            "chooseOptionBackScreenOption: $listOfFoodItem\n\n And Flag Value is -> $isEveryThingSetUp"
        )
        if (isEveryThingSetUp && listOfFoodItem.isNotEmpty()) {
            listOfFoodItem.addAll(args.list.foodList)
            val action = args.tbl?.let {
                DealsFragmentDirections.actionDealsFragmentToConfirmOderFragment(
                    FoodItemList(listOfFoodItem), it, args.confirm
                )
            } ?: DealsFragmentDirections.actionDealsFragmentToCostDashBoardFragment(
                FoodItemList(listOfFoodItem),
                args.confirm
            )
            findNavController().navigate(action)
        } else if (isEveryThingSetUp) {
            findNavController().popBackStack()
        }
    }

    private fun getDealsItemResponse() {
        viewModel.dealItemResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    binding.pbLayout.root.hide()
                    binding.swipeLayout.isRefreshing = false
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { err ->
                            showErrorMessage(err)
                        }
                    } else {
                        showErrorMessage("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    binding.swipeLayout.isRefreshing = true
                    binding.pbLayout.titleTxt.text = "${it.data}"
                    binding.pbLayout.root.show()
                }
                is ApisResponse.Success -> {
                    binding.pbLayout.root.hide()
                    binding.swipeLayout.isRefreshing = false
                    Log.i("DEALS", "getDealsItemResponse: ${it.data}")
                    val item = it.data as ScanAndFindDealsJson
                    dealsAddOnMenuItemAdaptor = DealsAdaptor(item.toList())
                    dealsAddOnMenuItemAdaptor?.onClickListener = this
                    binding.recycleItemView.adapter = dealsAddOnMenuItemAdaptor
                }
            }
        }
    }

    private fun getDealsResponse() {
        viewModel.dealsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    isEveryThingSetUp = true
                    binding.swipeLayout.isRefreshing = false
                    binding.pbLayout.root.hide()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { err ->
                            showErrorMessage(err)
                        }
                    } else {
                        showErrorMessage("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    binding.swipeLayout.isRefreshing = true
                    binding.pbLayout.titleTxt.text = "${it.data}"
                    binding.pbLayout.root.show()
                }
                is ApisResponse.Success -> {
                    isEveryThingSetUp = true
                    binding.swipeLayout.isRefreshing = false
                    binding.pbLayout.root.hide()
                    val value = (it.data as DealsJsonResponse)
                    dealsAddOnMenuAdaptor = DealsAdaptor(value.addOnMenu)
                    dealsAddOnMenuAdaptor?.onClickListener = this
                    binding.recycleItemView.adapter = dealsAddOnMenuAdaptor
                }
            }
        }
    }


    private fun showErrorMessage(msg: String) {
        showDialogBox("Failed", msg, icon = R.drawable.ic_error) {}
    }

    override fun onResume() {
        super.onResume()
        initial()
    }

    private fun initial() {
        currentDeals = null
        currentIcon = R.drawable.ic_arrow_back_24
        binding.topAppBar.title = "Deals"
        binding.topAppBar.setNavigationIcon(currentIcon)
        viewModel.getDeals()
    }

    override fun <T> onItemClicked(response: T) {
        if (response is AddOnMenu) {
            currentDeals = response
            binding.topAppBar.title = response.description
            currentIcon = R.drawable.ic_check
            binding.topAppBar.setNavigationIcon(currentIcon)
            viewModel.getScanDealApi(response.menuCode)
        } else if (response is Pair<*, *>) {
            val res = DealsStoreInstance.getInstance().addDealsItem(response.second as AddOnMenu)
            listOfFoodItem.add(res)
            activity?.msg("${(response.second as AddOnMenu).description} ${getEmojiByUnicode(0x2705)}")
        }
    }
}