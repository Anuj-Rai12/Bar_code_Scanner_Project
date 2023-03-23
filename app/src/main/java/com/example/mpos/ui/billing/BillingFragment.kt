package com.example.mpos.ui.billing

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.FoodAdaptor
import com.example.mpos.MainActivity
import com.example.mpos.R
import com.example.mpos.data.barcode.response.json.BarcodeJsonResponse
import com.example.mpos.data.billing.billingtoedc.BillingFromEDCRequest
import com.example.mpos.data.billing.billingtoedc.BillingToEdcRequestBody
import com.example.mpos.data.billing.conifrm_billing.ConfirmBillingRequest
import com.example.mpos.data.billing.conifrm_billing.ConfirmBillingRequestBody
import com.example.mpos.data.billing.printInvoice.json.PrintInvoice
import com.example.mpos.data.billing.printInvoice.request.PrintInvoiceRequest
import com.example.mpos.data.billing.printInvoice.request.PrintInvoiceRequestBody
import com.example.mpos.data.billing.send_billing_to_edc.ScanBillingToEdcRequest
import com.example.mpos.data.billing.send_billing_to_edc.ScanBillingToEdcRequestBody
import com.example.mpos.data.checkBillingStatus.CheckBillingStatusRequest
import com.example.mpos.data.checkBillingStatus.CheckBillingStatusRequestBody
import com.example.mpos.data.cofirmDining.ConfirmDiningBody
import com.example.mpos.data.cofirmDining.ConfirmDiningRequest
import com.example.mpos.data.confirmOrder.ConfirmOrderBody
import com.example.mpos.data.confirmOrder.ConfirmOrderRequest
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingJsonResponse
import com.example.mpos.data.generic.GenericDataCls
import com.example.mpos.data.generic.GenericDataCls.Companion.Type.*
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.BillingFragmentLayoutBinding
import com.example.mpos.payment.PaymentActivity
import com.example.mpos.ui.cost.viewmodel.CostDashBoardViewModel
import com.example.mpos.ui.crosselling.CrossSellingDialog
import com.example.mpos.ui.menu.bottomsheet.MenuBottomSheetFragment
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.oderconfirm.adaptor.ConfirmOderFragmentAdaptor
import com.example.mpos.ui.oderconfirm.view_model.ConfirmOrderFragmentViewModel
import com.example.mpos.ui.optionsbottomsheet.InfoBottomSheet
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.ui.searchfood.view_model.SearchFoodViewModel
import com.example.mpos.use_case.AlphaNumericString
import com.example.mpos.utils.*
import com.example.mpos.utils.print.recpit.PrintViewModel
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*
import kotlin.collections.ArrayList

class BillingFragment : Fragment(R.layout.billing_fragment_layout), OnBottomSheetClickListener {
    private lateinit var binding: BillingFragmentLayoutBinding
    private lateinit var confirmOderFragmentAdaptor: ConfirmOderFragmentAdaptor


    private val confirmOrderViewModel: ConfirmOrderFragmentViewModel by viewModels()
    private val viewModel: CostDashBoardViewModel by viewModels()
    private val searchViewModel: SearchFoodViewModel by viewModels()
    private val printBillViewModel: PrintViewModel by viewModels()


    private lateinit var searchFoodAdaptor: FoodAdaptor

    private var isPrinterConnected: Boolean = false
    private lateinit var callback: ItemTouchHelper.SimpleCallback
    private val args: BillingFragmentArgs by navArgs()
    private val arrItem = mutableListOf<ItemMasterFoodItem>()
    private val customDiningRequest: ConfirmDiningRequest = ConfirmDiningRequest(
        ConfirmDiningBody(
            screenType = RestaurantSingletonCls.getInstance().getScreenType()!!
        )
    )
    private var receiptNo: String? = null
    private var confirmBillingRequest: ConfirmBillingRequest? = null


    private var isOptionMnuVisible: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.semi_white_color_two)
        binding = BillingFragmentLayoutBinding.bind(view)
        binding.tableId2.text = args.selectioncls.title
        binding.qrCodeScan.setOnClickListener {
            val action = BillingFragmentDirections.actionGlobalScanQrCodeFragment(
                Url_Text,
                null,
                FoodItemList(arrItem),
                customDiningRequest,
                WhereToGoFromScan.BILLPAYMENT.name,
                args.selectioncls
            )
            initViewModel()
            findNavController().safeNavigate(action)
        }
        viewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { msg ->
                showErrorDialog(msg)
            }
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
        setInitialValue()
        getGrandTotal()
        getData()
        setCallBack()
        setRecycle()

        //Api CallBack
        getConfirmBillingResponse()
        getPosItemRequest()
        getConfirmOrderResponse()
        getSendBillToEdcResponse()
        getBillingToEdcResponse()


        //Check Bill Status
        getPrintConnectResponse()
        getCheckBillResponse()
        getPrintInvoiceResponse()
        getBillPrintResponse()

        //Set Search Adaptor
        setSearchAdaptorView()
        getItemSearchInfo()

        (activity as MainActivity?)?.getPermissionForBlueTooth()
        val flag = activity?.checkBlueConnectPermission()
        if (flag == false) {
            (activity as MainActivity?)?.requestPermission(
                Manifest.permission.BLUETOOTH_CONNECT, BLUE_CONNECT, "Bluetooth"
            )
        }


        binding.menuSearchEd.doOnTextChanged { txt, _, _, _ ->
            if (!txt.isNullOrEmpty()) {
                searchViewModel.searchQuery("%$txt%")
            }else{
                searchFoodAdaptor.submitList(listOf())
            }
        }

        binding.option.setOnClickListener {
            if (isOptionMnuVisible) {
                hideOptionMnu()
            } else {
                showOptionMenu()
            }
            isOptionMnuVisible = !isOptionMnuVisible
        }

        binding.foodMnuBtn.setOnClickListener {
            val mnuBottom = MenuBottomSheetFragment("Order Menu")
            mnuBottom.onBottomSheetClickListener = this
            mnuBottom.show(parentFragmentManager, MenuBottomSheetFragment.NAME)
        }
        binding.confirmOrderBtn.setOnClickListener {
            if (arrItem.isEmpty()) {
                viewModel.addError("Please Add Item Menu !!")
                return@setOnClickListener
            }
            if (setUpCostEstimation()) {
                viewModel.confirmBilling(confirmBillingRequest!!)
            } else {
                viewModel.addError("Oops cannot setUp a Process!!")
            }
        }


        binding.viewOfferBtn.setOnClickListener {
            val action = BillingFragmentDirections.actionGlobalDealsFragment(
                FoodItemList(arrItem),
                null,
                customDiningRequest,
                WhereToGoFromSearch.BILLPAYMENT.name,
                args.selectioncls
            )
            initViewModel()
            findNavController().safeNavigate(action)
        }

        binding.infoBtn.setOnClickListener {
            //Show Swipe dialog
            activity?.dialogOption(
                listOf(
                    "${getEmojiByUnicode(0x1F642)} About User", "${getEmojiByUnicode(0x1F4A1)} Help"
                ), this
            )
        }


        binding.foodMnuBtn.setOnLongClickListener {
            activity?.msg("Food Menu")
            return@setOnLongClickListener true
        }

        binding.checkStatusIc.setOnLongClickListener {
            activity?.msg("Check Bill Status")
            return@setOnLongClickListener true
        }

        binding.checkStatusIc.setOnClickListener {
            if (receiptNo != null) {
                printBillViewModel.isPrintConnected()
            } else {
                viewModel.addError("Cannot find Receipt No")
            }
        }

        binding.searchBoxTxt.setOnClickListener {
            //New Fragment
            Log.i(TAG, "Search: CustomerDining $customDiningRequest")
            Log.i(TAG, "Search: FoodItemList ${FoodItemList(arrItem)}")
            val action = BillingFragmentDirections.actionGlobalSearchFoodFragment(
                null,
                FoodItemList(arrItem),
                customDiningRequest,
                WhereToGoFromSearch.BILLPAYMENT.name,
                args.selectioncls
            )
            initViewModel()
            findNavController().safeNavigate(action)
        }


        binding.infoBtn.setOnLongClickListener {
            activity?.msg("Help")
            return@setOnLongClickListener true
        }


        binding.restItemBtn.setOnClickListener {
            DealsStoreInstance.getInstance().setIsResetButtonClick(true)
            arrItem.clear()
            confirmOrderViewModel.getGrandTotal(null)
            confirmOrderViewModel.removeItemFromListOrder()
            initial()
        }

    }

    private fun initViewModel() {
        confirmOrderViewModel.init()
        viewModel.init()
        printBillViewModel.init()
    }


    private fun setSearchAdaptorView() {
        binding.menuRecycle.apply {
            searchFoodAdaptor = FoodAdaptor {
                activity?.msg("$it")
                binding.menuSearchEd.setText("")
            }
            adapter = searchFoodAdaptor
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NotifyDataSetChanged")
    private fun getItemSearchInfo() {
        searchViewModel.fdInfo.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    //hideOrShow(null)
                    it.exception?.localizedMessage?.let { e ->
                        //showSnackBar(e, R.color.color_red, Snackbar.LENGTH_INDEFINITE)
                        activity?.msg(e)
                    }
                }
                is ApisResponse.Loading -> {}
                is ApisResponse.Success -> {
                    searchFoodAdaptor.notifyDataSetChanged()
                    val ls=it.data as List<ItemMaster>?
                    searchFoodAdaptor.submitList(ls)
                }
            }
        }
    }
    private fun setUpCostEstimation(): Boolean {
        receiptNo = AlphaNumericString.getAlphaNumericString(8)
        confirmBillingRequest = ConfirmBillingRequest(
            ConfirmBillingRequestBody(
                rcptNo = receiptNo ?: return false,
                transDate = getDate() ?: "2022-07-30",
                transTime = confirmOrderViewModel.time.value ?: "10:59 AM",
                storeVar = RestaurantSingletonCls.getInstance().getStoreId()!!,
                staffID = RestaurantSingletonCls.getInstance().getUserId()!!
            )
        )
        return true
    }


    override fun onResume() {
        super.onResume()
        showKeyBoard(binding.menuSearchEd)
    }

    override fun onPause() {
        super.onPause()
        activity?.hideKeyBoard(binding.menuSearchEd)
    }

    private fun getBillPrintResponse() {
        printBillViewModel.doPrintInvoicePrinting.observe(viewLifecycleOwner) {
            if (it != null) when (it) {
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
                    showDialogBox(
                        "Success", "${it.data}", icon = R.drawable.ic_success, isCancel = false
                    ) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun getPrintInvoiceResponse() {
        viewModel.printBillInvoice.observe(viewLifecycleOwner) {
            if (it != null) when (it) {
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
                    binding.restItemBtn.performClick()
                    /*arrItem.clear()
                    confirmOrderViewModel.getOrderList(null)*/
                    (it.data as PrintInvoice?)?.let { printInvoice ->
                        Log.i("PRINT_INVOICE", "getPrintInvoiceResponse: $printInvoice")
                        printBillViewModel.doPrintInvoice(printInvoice)
                    } ?: run {
                        showDialogBox(
                            "Success",
                            "Invoice Generate Successfully",
                            icon = R.drawable.ic_success,
                            isCancel = false
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun getPrintConnectResponse() {
        printBillViewModel.isPrinterConnected.observe(viewLifecycleOwner) {
            if (it != null) when (it) {
                is ApisResponse.Error -> {
                    hidePb()
                    isPrinterConnected = false
                    showDialogBox(
                        "Error", it.data!!, btn = "Yes", cancel = "No"
                    ) {
                        viewModel.checkBillingStatus(
                            CheckBillingStatusRequest(
                                CheckBillingStatusRequestBody(
                                    mPosDoc = receiptNo!!
                                )
                            )
                        )
                    }
                }
                is ApisResponse.Loading -> {
                    showPb("${it.data}")
                }
                is ApisResponse.Success -> {
                    hidePb()
                    isPrinterConnected = true
                    viewModel.checkBillingStatus(
                        CheckBillingStatusRequest(
                            CheckBillingStatusRequestBody(
                                mPosDoc = receiptNo!!
                            )
                        )
                    )
                }
            }
        }
    }


    private fun getConfirmBillingResponse() {
        viewModel.confirmBillingResponse.observe(viewLifecycleOwner) {
            if (it != null) when (it) {
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
                is ApisResponse.Loading -> showPb("${it.data}")
                is ApisResponse.Success -> {
                    hidePb()
                    confirmOrderViewModel.postLineUrl(receiptNo!!, arrItem)
                }
            }
        }
    }

    private fun getPosItemRequest() {
        confirmOrderViewModel.postLine.observe(viewLifecycleOwner) { pair ->
            pair?.second?.let {
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
                        confirmOrder(
                            ConfirmOrderRequest(
                                ConfirmOrderBody(
                                    pair.first
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
            if (it != null) when (it) {
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
                    val billObj = confirmBillingRequest?.body!!
                    //args.selectioncls.billingFromEDC
                    if (true) {
                        viewModel.sendBillingToEdcPaymentRequest(
                            BillingFromEDCRequest(
                                BillingToEdcRequestBody(
                                    rcptNo = receiptNo!!,
                                    transDate = billObj.transDate,
                                    transTime = billObj.transTime,
                                    storeVar = billObj.storeVar,
                                    staffID = billObj.staffID
                                )
                            )
                        )
                    } else {
                        viewModel.scanBillingRequest(
                            ScanBillingToEdcRequest(
                                ScanBillingToEdcRequestBody(
                                    rcptNo = receiptNo!!,
                                    transDate = billObj.transDate,
                                    transTime = billObj.transTime,
                                    storeVar = billObj.storeVar,
                                    staffID = billObj.staffID
                                )
                            )
                        )
                    }
                }
            }
        }
    }


    private fun getCheckBillResponse() {
        viewModel.checkBillingStatus.observe(viewLifecycleOwner) {
            if (it != null) when (it) {
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
                is ApisResponse.Loading -> showPb("${it.data}")
                is ApisResponse.Success -> {
                    hidePb()
                    viewModel.getPrintBillInvoiceResponse(
                        PrintInvoiceRequest(
                            PrintInvoiceRequestBody("${it.data}")
                        )
                    )
                }
            }
        }
    }


    private fun getBillingToEdcResponse() {
        viewModel.billingToEdc.observe(viewLifecycleOwner) {
            if (it != null) when (it) {
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
                is ApisResponse.Loading -> showPb("${it.data}")
                is ApisResponse.Success -> {
                    hidePb()
                    val intent = Intent(requireActivity(), PaymentActivity::class.java)
                    val pay = ArrayList<String>()
                    pay.clear()
                    pay.addAll(args.selectioncls.paymentLs)
                    intent.putExtra("Receipt", receiptNo)
                    intent.putExtra("upiCode", args.selectioncls.uPICode)
                    intent.putExtra("payment", pay)
                    intent.putExtra("tableNo", "1")
                    intent.putExtra("KOTPrintFromEDC", args.selectioncls.kotPrintFromEDC)
                    intent.putExtra("TBL_VALUE", args.selectioncls.apk)
                    startActivity(intent)
                }
            }
        }
    }


    private fun getSendBillToEdcResponse() {
        viewModel.sendBillingToEdc.observe(viewLifecycleOwner) {
            if (it != null) when (it) {
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
                is ApisResponse.Loading -> showPb("${it.data}")
                is ApisResponse.Success -> {
                    hidePb()
                    showDialogBox(
                        "Success",
                        "Completed the Billing for All Food Item Successfully",
                        icon = R.drawable.ic_success,
                        isCancel = true
                    ) {
                        //findNavController().popBackStack()
                    }
                }
            }
        }
    }


    private fun setRecycle() {
        binding.listOfItemRecycleView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            confirmOderFragmentAdaptor =
                ConfirmOderFragmentAdaptor(itemClickListerForFoodSelected = {},
                    itemClickListerForProcess = {
                        if (GenericDataCls.getBookingLs(it)
                                .isNotEmpty()
                        ) createBottomSheet("Select the Option.", GenericDataCls.getBookingLs(it))
                        else binding.root.showSandbar("Cannot Change the Content")
                    })
            adapter = confirmOderFragmentAdaptor
        }
    }

    private fun updateAmount(itemMasterFoodItem: ItemMasterFoodItem) {
        showQtyDialog(true,
            itemMasterFoodItem.itemMaster,
            type = "Amount",
            isDecimal = true,
            cancel = {},
            res = {},
            instruction = {},
            amount = {
                confirmOrderViewModel.addUpdateQty(
                    food = ItemMasterFoodItem(it, it.foodQty, it.foodAmt),
                    itemRemoved = itemMasterFoodItem
                )
            })
    }

    private fun updateQtyDialogBox(itemMasterFoodItem: ItemMasterFoodItem) {
        showQtyDialog(true,
            itemMasterFoodItem.itemMaster,
            isDecimal = itemMasterFoodItem.itemMaster.decimalAllowed.lowercase(
                Locale.getDefault()
            ).toBoolean(),
            cancel = {},
            res = {
                confirmOrderViewModel.addUpdateQty(
                    food = ItemMasterFoodItem(it, it.foodQty, it.foodAmt),
                    itemRemoved = itemMasterFoodItem
                )
            },
            instruction = {},
            amount = {})
    }


    private fun updateFreeTxt(itemMasterFoodItem: ItemMasterFoodItem) {
        showQtyDialog(true,
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
                    food = food, itemRemoved = itemMasterFoodItem
                )
            },
            isDecimal = false,
            amount = {})
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        confirmOrderViewModel.listOfOrder.observe(viewLifecycleOwner) {
            if (it != null) confirmOrderViewModel.getGrandTotal(it.data)
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

    private fun setCallBack() {
        callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                ).addSwipeLeftBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.color_red
                    )
                ).addSwipeLeftLabel("Delete").setSwipeLeftLabelColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                ).setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                    .addSwipeLeftActionIcon(R.drawable.ic_round_delete).setSwipeLeftActionIconTint(
                        ContextCompat.getColor(
                            requireContext(), R.color.white
                        )
                    ).create().decorate()



                super.onChildDraw(
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                )
            }

        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.listOfItemRecycleView)
    }

    private fun getGrandTotal() {
        confirmOrderViewModel.grandTotal.observe(viewLifecycleOwner) {
            binding.totalOrderAmt.text = it
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpRecycleAdaptor(data: List<ItemMasterFoodItem>) {
        binding.orderRecycleViewHint.hide()
        binding.listOfItemRecycleView.show()
        Log.i(TAG, "setUpRecycleAdaptor:  is Data empty ? ${data.isEmpty()}")
        confirmOderFragmentAdaptor.notifyDataSetChanged()
        confirmOderFragmentAdaptor.submitList(data)
    }

    private fun showErrorDialog(msg: String) {
        showDialogBox("Failed", msg, icon = R.drawable.ic_error) {}
    }

    private fun confirmOrder(confirmOrderRequest: ConfirmOrderRequest) {
        Log.i("confirmOrder", "$confirmOrderRequest")
        confirmOrderViewModel.saveUserOrderItem(confirmOrderRequest)
    }

    @SuppressLint("SetTextI18n")
    private fun initial() {
        binding.orderRecycleViewHint.show()
        binding.listOfItemRecycleView.hide()
    }

    private fun showPb(msg: String) {
        binding.pbLayout.root.show()
        binding.confirmOrderBtn.isEnabled = false
        binding.confirmOrderBtn.isClickable = false
        binding.pbLayout.titleTxt.text = msg
    }

    private fun setInitialValue() {
        val list = mutableListOf<ItemMasterFoodItem>()
        Log.i("ARRAY_P", "setInitialValue:ARGS ${args.list?.foodList?.size}  ---> ${args.list}")
        Log.i("ARRAY_P", "setInitialValue:ARRAY ${arrItem.size} ---> $arrItem")
        if (DealsStoreInstance.getInstance().isResetButtonClick()) {
            confirmOrderViewModel.getOrderList(null)
            return
        }
        if (args.list != null) {
            if (arrItem.isNotEmpty()) {
                list.addAll(arrItem)
            } else list.addAll(args.list?.foodList!!)

            confirmOrderViewModel.getOrderList(FoodItemList(list))
        } else if (args.list == null && arrItem.isNotEmpty()) {
            list.addAll(arrItem)
            confirmOrderViewModel.getOrderList(FoodItemList(list))
        } else if (args.list == null && list.isEmpty()) {
            confirmOrderViewModel.getOrderList(null)
        }
    }

    private fun hidePb() {
        binding.pbLayout.root.hide()
        binding.confirmOrderBtn.isEnabled = true
        binding.confirmOrderBtn.isClickable = true
    }


    private fun showOptionMenu() {
        activity?.let {
            val enterAnim = AnimationUtils.loadAnimation(activity, R.anim.enter_anim)
            enterAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    binding.option.setImageResource(R.drawable.ic_close_24)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
            if (args.selectioncls.dynamicMenuEnable) binding.foodMnuBtn.show()
            binding.checkStatusIc.show()
            binding.foodMnuBtn.animation = enterAnim
            binding.checkStatusIc.animation = enterAnim
        } ?: run {
            if (args.selectioncls.dynamicMenuEnable) binding.foodMnuBtn.show()
            binding.checkStatusIc.show()
            binding.option.setImageResource(R.drawable.ic_close_24)
        }
    }


    private fun hideOptionMnu() {
        activity?.let {
            val enterAnim = AnimationUtils.loadAnimation(activity, R.anim.pop_exit_anim)
            enterAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    binding.option.setImageResource(R.drawable.option_menu)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
            binding.foodMnuBtn.hide()
            binding.checkStatusIc.hide()
            binding.foodMnuBtn.animation = enterAnim
            binding.checkStatusIc.animation = enterAnim
        } ?: run {
            binding.foodMnuBtn.hide()
            binding.checkStatusIc.hide()
            binding.option.setImageResource(R.drawable.option_menu)
        }
    }


    private fun showSnackBar(msg: String, color: Int, length: Int = Snackbar.LENGTH_SHORT) {
        binding.root.showSandbar(
            msg, length, requireActivity().getColorInt(color)
        ) {
            return@showSandbar "OK"
        }
    }


    private fun <t> createBottomSheet(title: String, list: List<t>) {
        val bottomSheet = InfoBottomSheet(title, list)
        bottomSheet.listener = this
        bottomSheet.show(childFragmentManager, "Bottom Sheet")
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T> onItemClicked(response: T) {
        if (response is GenericDataCls) {
            navDialog(response)
            return
        }
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
        confirmOrderViewModel.getOrderList(FoodItemList(mutableList))
        activity?.msg(itemMaster.itemName + "\n${getEmojiByUnicode(0x2705)}")
    }

    private fun navDialog(info: GenericDataCls) {
        val data = info.data as ItemMasterFoodItem
        when (valueOf(info.type)) {
            ADDINSTRUCTION -> {
                updateFreeTxt(data)
            }
            UPDTQTY -> {
                updateQtyDialogBox(data)
            }
            UPDTAMTM -> updateAmount(data)
            VIEWORDER -> CrossSellingDialog.showCrossSellingItem(
                requireActivity(), data.crossSellingItems!!
            )
        }
    }
}
