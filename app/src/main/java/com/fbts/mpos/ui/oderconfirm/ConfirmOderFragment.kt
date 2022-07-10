package com.fbts.mpos.ui.oderconfirm

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fbts.mpos.R
import com.fbts.mpos.data.cofirmDining.ConfirmDiningRequest
import com.fbts.mpos.data.cofirmDining.response.ConfirmDiningSuccessResponse
import com.fbts.mpos.data.confirmOrder.ConfirmOrderBody
import com.fbts.mpos.data.confirmOrder.ConfirmOrderRequest
import com.fbts.mpos.data.confirmOrder.response.ConfirmOrderSuccessResponse
import com.fbts.mpos.databinding.ConfirmOrderLayoutBinding
import com.fbts.mpos.ui.menu.bottomsheet.MenuBottomSheetFragment
import com.fbts.mpos.ui.oderconfirm.adaptor.ConfirmOderFragmentAdaptor
import com.fbts.mpos.ui.oderconfirm.orderhistory.showDialogForOrderHistory
import com.fbts.mpos.ui.oderconfirm.view_model.ConfirmOrderFragmentViewModel
import com.fbts.mpos.ui.searchfood.model.FoodItemList
import com.fbts.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.fbts.mpos.use_case.AlphaNumericString
import com.fbts.mpos.utils.*
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class ConfirmOderFragment : Fragment(R.layout.confirm_order_layout) {
    private lateinit var binding: ConfirmOrderLayoutBinding
    private lateinit var confirmOderFragmentAdaptor: ConfirmOderFragmentAdaptor
    private val viewModel: ConfirmOrderFragmentViewModel by viewModels()
    private var flagForViewDeals: Boolean = false
    private lateinit var callback: ItemTouchHelper.SimpleCallback
    private val args: ConfirmOderFragmentArgs by navArgs()
    private val arrItem = mutableListOf<ItemMasterFoodItem>()
    private var receiptNo: String? = null
    private var customDiningRequest: ConfirmDiningRequest? = null
    private var isCustomerDiningRequestVisible: Boolean = true
    private var isOrderIsVisible = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color_two)
        binding = ConfirmOrderLayoutBinding.bind(view)
        binding.qrCodeScan.setOnClickListener {
            val action = ConfirmOderFragmentDirections
                .actionGlobalScanQrCodeFragment(
                    Url_Text,
                    args.tbl,
                    FoodItemList(arrItem),
                    customDiningRequest
                )
            findNavController().navigate(action)
        }

        viewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { map ->
                if (!map.values.first()) {
                    showSnackBar(map.keys.first(), R.color.color_red)
                } else {
                    showSnackBar(map.keys.first(), R.color.green_color)
                }
            }
        }

        setRecycle()
        setCallBack()

        binding.tblNumTxt.text = getString(R.string.sample_tbl_num, viewModel.getTbl(args.tbl))
        viewModel.time.observe(viewLifecycleOwner) {
            binding.orderBookingTimeTxt.text = getString(R.string.sample_tbl_time, "\n$it")
        }


        binding.foodMnuBtn.setOnClickListener {
            val mnuBottom = MenuBottomSheetFragment("Order Menu")
            mnuBottom.show(parentFragmentManager, MenuBottomSheetFragment.NAME)
        }


        viewModel.getOrderList(args.list)
        getData()
        getConfirmOrderResponse()
        getPosItemRequest()
        getConfirmDinningResponse()
        getGrandTotal()

        binding.viewOfferBtn.setOnClickListener {
            if (!flagForViewDeals) {
                flagForViewDeals = true
                getViewDeals()
                confirmOderFragmentAdaptor.setCheckBoxType(flagForViewDeals)
            } else {
                flagForViewDeals = false
                activity?.msg("No Offer Available")
                confirmOderFragmentAdaptor.setCheckBoxType(flagForViewDeals)
            }
        }

        binding.restItemBtn.setOnClickListener {
            arrItem.clear()
            viewModel.getGrandTotal(null)
            viewModel.removeItemFromListOrder()
            initial()
        }

        binding.searchBoxTxt.setOnClickListener {
            //New Fragment
            Log.i(TAG, "Search: Table ${args.tbl}")
            Log.i(TAG, "Search: CustomerDining $customDiningRequest")
            Log.i(TAG, "Search: FoodItemList ${FoodItemList(arrItem)}")
            val action =
                ConfirmOderFragmentDirections.actionConfirmOderFragmentToSearchFoodFragment(
                    args.tbl,
                    FoodItemList(arrItem),
                    customDiningRequest
                )
            findNavController().navigate(action)
        }

        binding.infoBtn.setOnClickListener {
            //Show Swipe dialog
            activity?.showDialogForDeleteInfo("${getEmojiByUnicode(0x1F5D1)} Swipe to delete")
        }

        binding.infoBtn.setOnLongClickListener {
            activity?.msg("Help")
            return@setOnLongClickListener true
        }

        binding.foodItem.setOnClickListener {
            //Get Dialog
            if (!isOrderIsVisible) {
                isOrderIsVisible = true
                val viewModel: ConfirmOrderFragmentViewModel by viewModels()
                activity?.showDialogForOrderHistory(
                    "${getEmojiByUnicode(0x1F372)} Order History",
                    args.tbl,
                    viewLifecycleOwner,
                    viewModel
                ) {
                    viewModel.getGrandTotal(arrItem)
                    isOrderIsVisible = false
                }
            }
        }


        binding.confirmOrderBtn.setOnClickListener {
            if (!receiptNo.isNullOrEmpty())
                viewModel.postLineUrl(receiptNo!!, arrItem)
            else
                activity?.msg("Oops Some thing Went Wrong Try Again?")
        }

    }


    private fun showErrorDialog(msg: String) {
        showDialogBox("Failed", msg, icon = R.drawable.ic_error) {}
    }


    private fun requestCustomerDining() {
        receiptNo = AlphaNumericString.getAlphaNumericString(8)
        isCustomerDiningRequestVisible = false
        val singletonCls = RestaurantSingletonCls.getInstance()
        activity?.addDialogMaterial(
            title = "Receipt No: $receiptNo",
            time = viewModel.time.value ?: "04:00 PM",
            tableNo = args.tbl.tableNo,
            receiptNo = receiptNo!!,
            storeVar = singletonCls.getStoreId()!!,
            staffID = singletonCls.getUserId()!!, cancel = {
                findNavController().popBackStack()
            }, listener = { res, flag ->
                if (flag) {
                    customDiningRequest = res
                    confirmDinningOrder(customDiningRequest!!)
                } else {
                    requestCustomerDining()
                }
            })
    }


    private fun getViewDeals() {
        viewModel.viewDeals.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                // Go To View Deals Fragments
                Log.i(TAG, "getViewDeals: ${it.size}")
            }
        }
    }

    private fun getGrandTotal() {
        viewModel.grandTotal.observe(viewLifecycleOwner) {
            binding.totalOrderAmt.text = it
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        viewModel.listOfOrder.observe(viewLifecycleOwner) {
            if (it != null)
                viewModel.getGrandTotal(it.data)
            else {
                viewModel.getGrandTotal(null)
            }
            when (it) {
                is ApisResponse.Error -> Log.i(TAG, "getData: Error")
                is ApisResponse.Loading -> if (it.data == null) {
                    arrItem.clear()
                    initial()
                }
                is ApisResponse.Success -> {
                    it.data?.let { data ->
                        arrItem.clear()
                        arrItem.addAll(data)
                        confirmOderFragmentAdaptor.setQtyBoxType(true)
                        setUpRecycleAdaptor(data)
                    }
                }
            }
        }
    }

    private fun setUpRecycleAdaptor(data: List<ItemMasterFoodItem>) {
        binding.orderRecycleViewHint.hide()
        binding.listOfItemRecycleView.show()
        confirmOderFragmentAdaptor.notifyDataSetChanged()
        confirmOderFragmentAdaptor.submitList(data)
    }


    private fun getConfirmDinningResponse() {
        viewModel.orderDining.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    Log.i("getConfirmDinningResponse", " Error ${it.exception}")
                    oopsSomeThingWentWrong()
                    binding.pbLayout.root.hide()
                }
                is ApisResponse.Loading -> {
                    Log.i("getConfirmDinningResponse", " Loading ${it.data}")
                    binding.pbLayout.root.show()
                    binding.pbLayout.titleTxt.text = it.data.toString()
                }
                is ApisResponse.Success -> {
                    Log.i("getConfirmDinningResponse", " Success ${it.data}")
                    binding.pbLayout.root.hide()
                    (it.data as ConfirmDiningSuccessResponse?)?.let { res ->
                        val error = res.body?.errorFound?.toBoolean()
                        val errorBdy = res.body?.errorText.toString()
                        if (error == true) {
                            val result = Pair("Failed!", R.drawable.ic_error)
                            showDialogBox(result.first, errorBdy, icon = result.second) {}
                        } else {
                            showDialogBox(
                                "Success",
                                "Table No:${args.tbl.tableNo}\nStatus: Occupied\nReceipt no:$receiptNo",
                                icon = R.drawable.ic_success
                            ) {}
                        }
                    } ?: run {
                        oopsSomeThingWentWrong()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        customDiningRequest = args.confirmreq
        receiptNo = if (customDiningRequest?.body?.rcptNo != null) {
            customDiningRequest?.body?.rcptNo.toString()
        } else if (args.tbl.receiptNo.isNotEmpty()) {
            binding.foodItem.show()
            args.tbl.receiptNo
        } else {
            null
        }


        if (customDiningRequest == null && args.tbl.receiptNo.isEmpty() && isCustomerDiningRequestVisible) {
            requestCustomerDining()
        }
    }

    private fun getPosItemRequest() {
        viewModel.postLine.observe(viewLifecycleOwner) { pair ->
            pair.second.let {
                when (it) {
                    is ApisResponse.Error -> {
                        binding.pbLayout.root.hide()
                        if (it.data == null) {
                            it.exception?.localizedMessage?.let { msg ->
                                showErrorDialog(msg)
                            }
                        } else {
                            showErrorDialog("${it.data}")
                        }
                    }
                    is ApisResponse.Loading -> {
                        binding.pbLayout.titleTxt.text = "${it.data}"
                        binding.pbLayout.root.show()
                    }
                    is ApisResponse.Success -> {
                        binding.pbLayout.root.hide()
                        Log.i(TAG, "getPosItemRequest: PosItem Response ${it.data}")
                        //Add ConfirmOrder Request
                        confirmOrder(ConfirmOrderRequest(ConfirmOrderBody(pair.first)))
                    }
                }
            }
        }
    }


    private fun getConfirmOrderResponse() {
        viewModel.orderConfirm.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    Log.i("getConfirmOrderResponse", " Error ${it.exception}")
                    oopsSomeThingWentWrong()
                    binding.pbLayout.root.hide()
                }
                is ApisResponse.Loading -> {
                    Log.i("getConfirmOrderResponse", " Loading ${it.data}")
                    binding.pbLayout.root.show()
                    binding.pbLayout.titleTxt.text = it.data.toString()
                }
                is ApisResponse.Success -> {
                    binding.pbLayout.root.hide()
                    (it.data as ConfirmOrderSuccessResponse?)?.let { res ->
                        val str = res.body?.returnValue
                        val result = if (str == "01") {
                            Pair(
                                R.drawable.ic_error, Pair(
                                    "Failed!",
                                    "Order is Not Inserted in Navision at All."
                                )
                            )
                        } else {
                            Pair(
                                R.drawable.ic_success, Pair(
                                    "Successfully Inserted",
                                    "Order is Inserted in Navision at All."
                                )
                            )
                        }
                        showDialogBox(
                            result.second.first,
                            result.second.second,
                            icon = result.first
                        ) {
                            if (str != "01") {
                                findNavController().popBackStack()
                            }
                        }
                    } ?: run {
                        val res =
                            Pair("Failed!", "Order is Not Inserted in Navision at All.")
                        showDialogBox(res.first, res.second, icon = R.drawable.ic_error) {}
                    }
                }
            }
        }
    }

    private fun oopsSomeThingWentWrong() {
        showSnackBar(msg = "Oops Something Went Wrong..", color = R.color.color_red)
    }

    private fun confirmDinningOrder(confirmDiningRequest: ConfirmDiningRequest) {
        Log.i("confirmDinningOrder", "$confirmDiningRequest")
        viewModel.updateAndLockTbl(confirmDiningRequest)
    }

    private fun confirmOrder(confirmOrderRequest: ConfirmOrderRequest) {
        Log.i("confirmOrder", "$confirmOrderRequest")
        viewModel.saveUserOrderItem(confirmOrderRequest)
    }

    @SuppressLint("SetTextI18n")
    private fun initial() {
        binding.orderRecycleViewHint.show()
        binding.listOfItemRecycleView.hide()
    }

    private fun setCallBack() {
        callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false


            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val getItem =
                    confirmOderFragmentAdaptor.currentList[viewHolder.absoluteAdapterPosition]
                getItem?.let {
                    viewModel.deleteSwipe(it)
                }
            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_red
                        )
                    )
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                    .addSwipeLeftActionIcon(R.drawable.ic_round_delete)
                    .setSwipeLeftActionIconTint(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    .create()
                    .decorate()



                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.listOfItemRecycleView)
    }


    private fun setRecycle() {
        binding.listOfItemRecycleView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            confirmOderFragmentAdaptor =
                ConfirmOderFragmentAdaptor(itemClickListerForFoodSelected = {
                    if (flagForViewDeals)
                        viewModel.getOrderItem(it)
                }, itemClickListerForUpdate = { res ->
                    updateQtyDialogBox(res)
                })
            adapter = confirmOderFragmentAdaptor
        }
    }

    private fun updateQtyDialogBox(itemMasterFoodItem: ItemMasterFoodItem) {
        showQtyDialog(true, itemMasterFoodItem.itemMaster, cancel = {}, res = {
            viewModel.addUpdateQty(
                food = ItemMasterFoodItem(it, it.foodQty, it.foodAmt),
                itemRemoved = itemMasterFoodItem
            )
        })
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun showSnackBar(msg: String, color: Int, length: Int = Snackbar.LENGTH_SHORT) {
        binding.root.showSandbar(
            msg,
            length,
            requireActivity().getColorInt(color)
        ) {
            return@showSandbar "OK"
        }
    }
}