package com.example.mpos.ui.oderconfirm

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.R
import com.example.mpos.data.barcode.response.json.BarcodeJsonResponse
import com.example.mpos.data.cofirmDining.ConfirmDiningRequest
import com.example.mpos.data.cofirmDining.response.ConfirmDiningSuccessResponse
import com.example.mpos.data.confirmOrder.ConfirmOrderBody
import com.example.mpos.data.confirmOrder.ConfirmOrderRequest
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingJsonResponse
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.data.printbIll.PrintBillRequest
import com.example.mpos.data.printbIll.PrintBillRequestBody
import com.example.mpos.databinding.ConfirmOrderLayoutBinding
import com.example.mpos.ui.menu.bottomsheet.MenuBottomSheetFragment
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.oderconfirm.adaptor.ConfirmOderFragmentAdaptor
import com.example.mpos.ui.oderconfirm.orderhistory.showDialogForOrderHistory
import com.example.mpos.ui.oderconfirm.view_model.ConfirmOrderFragmentViewModel
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.use_case.AlphaNumericString
import com.example.mpos.utils.*
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*


class ConfirmOderFragment : Fragment(R.layout.confirm_order_layout), OnBottomSheetClickListener {
    private lateinit var binding: ConfirmOrderLayoutBinding
    private lateinit var confirmOderFragmentAdaptor: ConfirmOderFragmentAdaptor
    private val viewModel: ConfirmOrderFragmentViewModel by viewModels()

    //private var flagForViewDeals: Boolean = false
    private lateinit var callback: ItemTouchHelper.SimpleCallback
    private val args: ConfirmOderFragmentArgs by navArgs()
    private val arrItem = mutableListOf<ItemMasterFoodItem>()
    private var receiptNo: String? = null
    private var customDiningRequest: ConfirmDiningRequest? = null
    private var isCustomerDiningRequestVisible: Boolean = true
    private var isOrderIsVisible = false

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
                    customDiningRequest, WhereToGoFromScan.TABLEMANGMENT.name
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
            mnuBottom.onBottomSheetClickListener = this
            mnuBottom.show(parentFragmentManager, MenuBottomSheetFragment.NAME)
        }
        setInitialValue()
        getData()
        getConfirmOrderResponse()
        getPosItemRequest()
        getConfirmDinningResponse()
        getGrandTotal()

        binding.viewOfferBtn.setOnClickListener {
            val action =
                ConfirmOderFragmentDirections.actionGlobalDealsFragment(
                    FoodItemList(arrItem),
                    args.tbl,
                    customDiningRequest, WhereToGoFromSearch.TABLEMANGMENT.name
                )
            findNavController().navigate(action)
        }

        binding.restItemBtn.setOnClickListener {
            arrItem.clear()
            viewModel.getGrandTotal(null)
            viewModel.removeItemFromListOrder()
            DealsStoreInstance.getInstance().setIsResetButtonClick(true)
            initial()
        }

        binding.searchBoxTxt.setOnClickListener {
            //New Fragment
            Log.i(TAG, "Search: Table ${args.tbl}")
            Log.i(TAG, "Search: CustomerDining $customDiningRequest")
            Log.i(TAG, "Search: FoodItemList ${FoodItemList(arrItem)}")
            val action =
                ConfirmOderFragmentDirections.actionGlobalSearchFoodFragment(
                    args.tbl,
                    FoodItemList(arrItem),
                    customDiningRequest,
                    WhereToGoFromSearch.TABLEMANGMENT.name
                )
            findNavController().navigate(action)
        }

        binding.infoBtn.setOnClickListener {
            //Show Swipe dialog
            activity?.dialogOption(listOf("About User", "Help"), this)
        }

        binding.infoBtn.setOnLongClickListener {
            activity?.msg("Help")
            return@setOnLongClickListener true
        }

        showPrintResponse()

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
                    if (it) {
                        viewModel.getGrandTotal(arrItem)
                        viewModel.fetchPrintResponse(PrintBillRequest(PrintBillRequestBody(args.tbl.receiptNo)))
                    }
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

    private fun setInitialValue() {
        val list = mutableListOf<ItemMasterFoodItem>()
        if (DealsStoreInstance.getInstance().isResetButtonClick()) {
            viewModel.getOrderList(null)
            return
        }
        if (args.list != null) {
            if (arrItem.isNotEmpty()) {
                if (!args.list?.foodList?.containsAll(arrItem)!!) {
                    list.addAll(arrItem)
                } else
                    list.addAll(arrItem)
            } else
                list.addAll(args.list?.foodList!!)

            viewModel.getOrderList(FoodItemList(list))
        } else if (args.list == null && arrItem.isNotEmpty()) {
            list.addAll(arrItem)
            viewModel.getOrderList(FoodItemList(list))
        } else if (args.list == null && list.isEmpty()) {
            viewModel.getOrderList(null)
        }
    }

    private fun showPrintResponse() {
        viewModel.printBill.observe(viewLifecycleOwner) { ev ->
            ev.getContentIfNotHandled()?.let {
                when (it) {
                    is ApisResponse.Error -> {
                        hidePb()
                        if (it.data == null) {
                            it.exception?.localizedMessage?.let { res ->
                                showErrorDialog(res)
                            }
                        } else {
                            showErrorDialog(it.data)
                        }
                    }
                    is ApisResponse.Loading -> {
                        showPb("${it.data}")
                    }
                    is ApisResponse.Success -> {
                        hidePb()
                        if (it.data?.startsWith("01")!!) {
                            activity?.msg(
                                "order Printed ${getEmojiByUnicode(0x1F5A8)}",
                                Toast.LENGTH_LONG
                            )
                            findNavController().popBackStack()
                        } else {
                            activity?.msg("failed ${it.data}", Toast.LENGTH_LONG)
                        }
                    }
                }
            }
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
        Log.i(TAG, "setUpRecycleAdaptor:  is Data empty ? ${data.isEmpty()}")
        confirmOderFragmentAdaptor.notifyDataSetChanged()
        confirmOderFragmentAdaptor.submitList(data)
    }


    private fun getConfirmDinningResponse() {
        viewModel.orderDining.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    Log.i("getConfirmDinningResponse", " Error ${it.exception}")
                    oopsSomeThingWentWrong()
                    hidePb()
                }
                is ApisResponse.Loading -> {
                    Log.i("getConfirmDinningResponse", " Loading ${it.data}")
                    showPb(it.data.toString())
                }
                is ApisResponse.Success -> {
                    Log.i("getConfirmDinningResponse", " Success ${it.data}")
                    hidePb()
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
                        hidePb()
                        if (it.data == null) {
                            it.exception?.localizedMessage?.let { msg ->
                                showErrorDialog(msg)
                            }
                        } else {
                            showErrorDialog("${it.data}")
                        }
                    }
                    is ApisResponse.Loading -> {
                        showPb("${it.data}")
                    }
                    is ApisResponse.Success -> {
                        hidePb()
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
                    //Log.i("getConfirmOrderResponse", " Error ${it.exception}")
                    hidePb()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { err ->
                            showErrorDialog(err)
                        }
                    } else {
                        showErrorDialog("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    Log.i("getConfirmOrderResponse", " Loading ${it.data}")
                    showPb(it.data.toString())
                }
                is ApisResponse.Success -> {
                    hidePb()
                    showDialogBox(
                        "Successfully Inserted",
                        "${it.data}",
                        icon = R.drawable.ic_success,
                        isCancel = false
                    ) {
                        var isTrue = true
                        val handler = Handler(Looper.getMainLooper())
                        handler.post {
                            if (isTrue) {
                                findNavController().popBackStack()
                                isTrue = false
                            }
                        }
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

                }, itemClickListerForUpdate = { res ->
                    updateQtyDialogBox(res)
                }, itemClickInstructionLinter = { res ->
                    updateFreeTxt(res)
                }, itemClickAmountLinter = { res ->
                    updateAmount(res)
                }, context = requireActivity())
            adapter = confirmOderFragmentAdaptor
        }
    }

    private fun updateFreeTxt(itemMasterFoodItem: ItemMasterFoodItem) {
        showQtyDialog(
            true,
            itemMasterFoodItem.itemMaster,
            value = itemMasterFoodItem.free_txt,
            type = "Instruction",
            cancel = {},
            res = {},
            instruction = { free_txt ->
                val it = itemMasterFoodItem.itemMaster
                viewModel.addUpdateQty(
                    food = ItemMasterFoodItem(it, it.foodQty, it.foodAmt, free_txt = free_txt),
                    itemRemoved = itemMasterFoodItem
                )
            }, isDecimal = false, amount = {})
    }

    private fun updateAmount(itemMasterFoodItem: ItemMasterFoodItem) {
        showQtyDialog(true,
            itemMasterFoodItem.itemMaster,
            type = "Amount",
            isDecimal = true,
            cancel = {},
            res = {},
            instruction = {}, amount = {
                viewModel.addUpdateQty(
                    food = ItemMasterFoodItem(it, it.foodQty, it.foodAmt),
                    itemRemoved = itemMasterFoodItem
                )
            })
    }

    private fun updateQtyDialogBox(itemMasterFoodItem: ItemMasterFoodItem) {
        showQtyDialog(
            true,
            itemMasterFoodItem.itemMaster,
            cancel = {},
            res = {
                viewModel.addUpdateQty(
                    food = ItemMasterFoodItem(it, it.foodQty, it.foodAmt),
                    itemRemoved = itemMasterFoodItem
                )
            },
            instruction = { },
            isDecimal = itemMasterFoodItem.itemMaster.decimalAllowed.lowercase(Locale.getDefault())
                .toBoolean(), amount = {})
    }


    private fun showSnackBar(msg: String, color: Int, length: Int = Snackbar.LENGTH_SHORT) {
        binding.root.showSandbar(
            msg,
            length,
            requireActivity().getColorInt(color)
        ) {
            return@showSandbar "OK"
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> onItemClicked(response: T) {
        val barcode = (response as Pair<*, *>).first as BarcodeJsonResponse
        val crossSellingItems =
            (response as Pair<*, *>).second as Pair<Double, CrossSellingJsonResponse>?
        Log.i(TAG, "onItemClicked: $response")
        val itemMaster = ItemMaster(
            barcode = barcode.barcode,
            id = 0,
            itemCategory = barcode.itemCategory,
            salePrice = barcode.salePrice,
            itemDescription = barcode.itemDescription,
            itemCode = barcode.itemCode,
            itemName = barcode.itemName,
            uOM = barcode.uOM,
            decimalAllowed = barcode.decimalAllowed,
            crossSellingAllow = barcode.crossSellingAllow
        )
        itemMaster.foodQty = barcode.qty.toDouble()
        val amt =
            (ListOfFoodItemToSearchAdaptor.setPrice(itemMaster.salePrice) * itemMaster.foodQty)
        itemMaster.foodAmt = "%.4f".format(amt).toDouble() + (crossSellingItems?.first ?: 0.0)
        val mutableList = mutableListOf<ItemMasterFoodItem>()
        if (arrItem.isNotEmpty()) {
            mutableList.addAll(arrItem)
        }
        mutableList.add(
            ItemMasterFoodItem(
                itemMaster = itemMaster,
                foodQty = itemMaster.foodQty,
                foodAmt = itemMaster.foodAmt,
                crossSellingItems = crossSellingItems?.second,
                bg = if (crossSellingItems?.second != null) listOfBg[2]
                else listOfBg.first()
            )
        )
        viewModel.getOrderList(FoodItemList(mutableList))
        activity?.msg(itemMaster.itemName + "\n${getEmojiByUnicode(0x2705)}")
    }

    private fun showPb(msg: String) {
        binding.pbLayout.root.show()
        binding.confirmOrderBtn.isEnabled = false
        binding.confirmOrderBtn.isClickable = false
        binding.pbLayout.titleTxt.text = msg
    }

    private fun hidePb() {
        binding.pbLayout.root.hide()
        binding.confirmOrderBtn.isEnabled = true
        binding.confirmOrderBtn.isClickable = true
    }
}