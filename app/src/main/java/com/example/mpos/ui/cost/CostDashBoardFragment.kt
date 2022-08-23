package com.example.mpos.ui.cost

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
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
import com.example.mpos.MainActivity
import com.example.mpos.R
import com.example.mpos.data.barcode.response.json.BarcodeJsonResponse
import com.example.mpos.data.cofirmDining.ConfirmDiningRequest
import com.example.mpos.data.confirmOrder.ConfirmOrderBody
import com.example.mpos.data.confirmOrder.ConfirmOrderRequest
import com.example.mpos.data.confirmOrder.response.json.PrintReceiptInfo
import com.example.mpos.data.costestimation.request.ConfirmEstimationBody
import com.example.mpos.data.costestimation.request.CostEstimation
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.CostCalDashbordLayoutBinding
import com.example.mpos.ui.cost.viewmodel.CostDashBoardViewModel
import com.example.mpos.ui.menu.bottomsheet.MenuBottomSheetFragment
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.oderconfirm.adaptor.ConfirmOderFragmentAdaptor
import com.example.mpos.ui.oderconfirm.view_model.ConfirmOrderFragmentViewModel
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.use_case.AlphaNumericString
import com.example.mpos.utils.*
import com.example.mpos.utils.print.PrintBill
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class CostDashBoardFragment : Fragment(R.layout.cost_cal_dashbord_layout),
    OnBottomSheetClickListener {
    private lateinit var binding: CostCalDashbordLayoutBinding

    private lateinit var confirmOderFragmentAdaptor: ConfirmOderFragmentAdaptor

    private lateinit var printBill: PrintBill
    private val confirmOrderViewModel: ConfirmOrderFragmentViewModel by viewModels()
    private val costEstimationViewModel: CostDashBoardViewModel by viewModels()


    private var flagForViewDeals: Boolean = false
    private lateinit var callback: ItemTouchHelper.SimpleCallback
    private val args: CostDashBoardFragmentArgs by navArgs()
    private val arrItem = mutableListOf<ItemMasterFoodItem>()
    private var customDiningRequest: ConfirmDiningRequest? = null
    private var receiptNo: String? = null
    private var costEstimation: CostEstimation? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.semi_white_color_two)
        binding = CostCalDashbordLayoutBinding.bind(view)
        binding.qrCodeScan.setOnClickListener {
            val action = CostDashBoardFragmentDirections
                .actionGlobalScanQrCodeFragment(
                    Url_Text,
                    null,
                    FoodItemList(arrItem),
                    customDiningRequest
                )
            findNavController().navigate(action)
        }

        confirmOrderViewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { map ->
                if (!map.values.first()) {
                    showSnackBar(map.keys.first(), R.color.color_red)
                } else {
                    showSnackBar(map.keys.first(), R.color.green_color)
                }
            }
        }


        costEstimationViewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { msg ->
                showErrorDialog(msg)
            }
        }

        initializePrinter()

        binding.foodMnuBtn.setOnClickListener {
            val mnuBottom = MenuBottomSheetFragment("Order Menu")
            mnuBottom.onBottomSheetClickListener = this
            mnuBottom.show(parentFragmentManager, MenuBottomSheetFragment.NAME)
        }
        setInitialValue()
        getGrandTotal()
        getData()
        setCallBack()
        setRecycle()

        //All Api CallBack
        getCostEstimationResponse()
        getPosItemRequest()
        getConfirmOrderResponse()
        (activity as MainActivity?)?.getPermissionForBlueTooth()
        binding.confirmOrderBtn.setOnClickListener {
            if (!this::printBill.isInitialized) {
                initializePrinter()
                activity?.msg("Failed to Set Up Try Again!!")
                return@setOnClickListener
            }
            if (arrItem.isEmpty()) {
                costEstimationViewModel.addError("Please Add Item Menu !!")
                return@setOnClickListener
            }
            if (setUpCostEstimation()) {
                if (!printBill.isBluetoothDeviceFound()) {
                    showDialogBox(
                        "Error",
                        printBill.printNotConnectedToProceed,
                        btn = "Yes",
                        cancel = "No"
                    ) {
                        costEstimationViewModel.getCostEstimation(costEstimation!!)
                    }
                } else {
                    costEstimationViewModel.getCostEstimation(costEstimation!!)
                }
            } else {
                costEstimationViewModel.addError("Oops cannot setUp a Process!!")
            }
        }

        binding.infoBtn.setOnClickListener {
            //Show Swipe dialog
            activity?.showDialogForDeleteInfo("${getEmojiByUnicode(0x1F5D1)} Swipe to delete")
        }

        binding.searchBoxTxt.setOnClickListener {
            //New Fragment
            Log.i(TAG, "Search: CustomerDining $customDiningRequest")
            Log.i(TAG, "Search: FoodItemList ${FoodItemList(arrItem)}")
            val action =
                CostDashBoardFragmentDirections.actionCostDashBoardFragmentToSearchFoodFragment(
                    null,
                    FoodItemList(arrItem),
                    customDiningRequest
                )
            findNavController().navigate(action)
        }


        binding.infoBtn.setOnLongClickListener {
            activity?.msg("Help")
            return@setOnLongClickListener true
        }


        binding.restItemBtn.setOnClickListener {
            arrItem.clear()
            confirmOrderViewModel.getGrandTotal(null)
            confirmOrderViewModel.removeItemFromListOrder()
            initial()
        }

    }

    private fun initializePrinter() {
        activity?.let {
            printBill = PrintBill(it) { err ->
                costEstimationViewModel.addError(err)
            }
        } ?: costEstimationViewModel.addError(
            "Cannot Initialized Printer ${
                getEmojiByUnicode(
                    0x1F5A8
                )
            }"
        )
    }

    private fun getCostEstimationResponse() {
        costEstimationViewModel.costEstimationResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    hidePb()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { err ->
                            showErrorDialog(err)
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
                    confirmOrderViewModel.postLineUrl(receiptNo!!, arrItem)
                }
            }
        }
    }


    private fun getPosItemRequest() {
        confirmOrderViewModel.postLine.observe(viewLifecycleOwner) { pair ->
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
                        binding.pbLayout.root.hide()
                        Log.i(TAG, "getPosItemRequest: PosItem Response ${it.data}")
                        //Add ConfirmOrder Request
                        confirmOrder(
                            ConfirmOrderRequest(
                                ConfirmOrderBody(
                                    pair.first,
                                    true.toString()
                                )
                            )
                        )
                    }
                }
            }
        }
    }


    private fun getConfirmOrderResponse() {
        confirmOrderViewModel.orderConfirm.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
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
                    showPb("${it.data}")
                }
                is ApisResponse.Success -> {
                    hidePb()
                    arrItem.clear()
                    confirmOrderViewModel.getOrderList(null)
                    (it.data as PrintReceiptInfo?)?.let { body ->
                        if (body.itemList.isEmpty()) {
                            showDialogBox(
                                "Success",
                                "All Food Item are added Successfully",
                                icon = R.drawable.ic_success
                            ) {}
                            activity?.msg("Bill List is Empty",Toast.LENGTH_LONG)
                        } else if (printBill.isBluetoothDeviceFound())
                            printBill.doPrint(body)
                        else
                            showDialogBox(
                                "Success",
                                "All Food Item are added Successfully",
                                icon = R.drawable.ic_success
                            ) {}
                    } ?: showErrorDialog("Cannot Print Bill")
                }
            }
        }
    }


    private fun showErrorDialog(msg: String) {
        showDialogBox("Failed", msg, icon = R.drawable.ic_error) {}
    }


    private fun showPb(msg: String) {
        binding.pbLayout.root.show()
        binding.pbLayout.titleTxt.text = msg
    }

    private fun hidePb() {
        binding.pbLayout.root.hide()
    }


    private fun setUpCostEstimation(): Boolean {
        receiptNo = AlphaNumericString.getAlphaNumericString(8)
        costEstimation = CostEstimation(
            ConfirmEstimationBody(
                rcptNo = receiptNo ?: return false,
                transDate = getDate() ?: "2022-07-30",
                transTime = confirmOrderViewModel.time.value ?: "10:59 AM",
                salesType = AllStringConst.API.RESTAURANT.name,
                storeVar = RestaurantSingletonCls.getInstance().getStoreId()!!,
                contactNo = "random number",
                terminalNo = "",
                staffID = RestaurantSingletonCls.getInstance().getUserId()!!
            )
        )
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        confirmOrderViewModel.listOfOrder.observe(viewLifecycleOwner) {
            if (it != null)
                confirmOrderViewModel.getGrandTotal(it.data)
            else {
                confirmOrderViewModel.getGrandTotal(null)
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

    private fun setInitialValue() {
        val list = mutableListOf<ItemMasterFoodItem>()
        if (args.list != null) {
            if (arrItem.isNotEmpty()) {
                if (!args.list?.foodList?.containsAll(arrItem)!!) {
                    list.addAll(arrItem)
                }
            }
            list.addAll(args.list?.foodList!!)
            confirmOrderViewModel.getOrderList(FoodItemList(list))
        } else if (args.list == null && arrItem.isNotEmpty()) {
            list.addAll(arrItem)
            confirmOrderViewModel.getOrderList(FoodItemList(list))
        } else if (args.list == null && list.isEmpty()) {
            confirmOrderViewModel.getOrderList(null)
        }
    }

    private fun getGrandTotal() {
        confirmOrderViewModel.grandTotal.observe(viewLifecycleOwner) {
            binding.totalOrderAmt.text = it
        }
    }

    private fun setRecycle() {
        binding.listOfItemRecycleView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            confirmOderFragmentAdaptor =
                ConfirmOderFragmentAdaptor(itemClickListerForFoodSelected = {
                    if (flagForViewDeals)
                        confirmOrderViewModel.getOrderItem(it)
                }, itemClickListerForUpdate = { res ->
                    updateQtyDialogBox(res)
                }, itemClickInstructionLinter = { res ->
                    updateFreeTxt(res)
                })
            adapter = confirmOderFragmentAdaptor
        }
    }

    private fun updateFreeTxt(itemMasterFoodItem: ItemMasterFoodItem) {
        showQtyDialog(
            true,
            itemMasterFoodItem.itemMaster,
            type = "Instruction",
            value = itemMasterFoodItem.free_txt,
            cancel = {},
            res = {},
            instruction = { free_txt ->
                val it = itemMasterFoodItem.itemMaster
                val food = ItemMasterFoodItem(it, it.foodQty, it.foodAmt, free_txt = free_txt)
                Log.i(TAG, "updateQtyDialogBox: $food")
                confirmOrderViewModel.addUpdateQty(
                    food = food,
                    itemRemoved = itemMasterFoodItem
                )
            })
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
                    confirmOrderViewModel.deleteSwipe(it)
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

    /*private fun oopsSomeThingWentWrong() {
        showSnackBar(msg = "Oops Something Went Wrong..", color = R.color.color_red)
    }

    private fun confirmDinningOrder(confirmDiningRequest: ConfirmDiningRequest) {
        Log.i("confirmDinningOrder", "$confirmDiningRequest")
        viewModel.updateAndLockTbl(confirmDiningRequest)
    }
    */
    private fun confirmOrder(confirmOrderRequest: ConfirmOrderRequest) {
        Log.i("confirmOrder", "$confirmOrderRequest")
        confirmOrderViewModel.saveUserOrderItem(confirmOrderRequest)
    }

    @SuppressLint("SetTextI18n")
    private fun initial() {
        binding.orderRecycleViewHint.show()
        binding.listOfItemRecycleView.hide()
    }

    private fun updateQtyDialogBox(itemMasterFoodItem: ItemMasterFoodItem) {
        showQtyDialog(true, itemMasterFoodItem.itemMaster, cancel = {}, res = {
            confirmOrderViewModel.addUpdateQty(
                food = ItemMasterFoodItem(it, it.foodQty, it.foodAmt),
                itemRemoved = itemMasterFoodItem
            )
        }, instruction = {})
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

    override fun <T> onItemClicked(response: T) {
        val barcode = response as BarcodeJsonResponse
        Log.i(TAG, "onItemClicked: $response")
        val itemMaster = ItemMaster(
            barcode = barcode.barcode,
            id = 0,
            itemCategory = barcode.itemCategory,
            salePrice = barcode.salePrice,
            itemDescription = barcode.itemDescription,
            itemCode = barcode.itemCode,
            itemName = barcode.itemName,
            uOM = barcode.uOM
        )
        itemMaster.foodQty = barcode.qty
        itemMaster.foodAmt =
            ListOfFoodItemToSearchAdaptor.setPrice(itemMaster.salePrice) * itemMaster.foodQty
        val mutableList = mutableListOf<ItemMasterFoodItem>()
        if (arrItem.isNotEmpty()) {
            mutableList.addAll(arrItem)
        }
        mutableList.add(ItemMasterFoodItem(itemMaster, itemMaster.foodQty, itemMaster.foodAmt))
        confirmOrderViewModel.getOrderList(FoodItemList(mutableList))
        activity?.msg(itemMaster.itemName + "\n${getEmojiByUnicode(0x2705)}")
    }
}