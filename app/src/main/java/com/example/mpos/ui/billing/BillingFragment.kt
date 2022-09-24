package com.example.mpos.ui.billing

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.BillingFragmentLayoutBinding
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
import com.example.mpos.utils.print.recpit.PrintViewModel
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*

class BillingFragment : Fragment(R.layout.billing_fragment_layout), OnBottomSheetClickListener {
    private lateinit var binding: BillingFragmentLayoutBinding
    private lateinit var confirmOderFragmentAdaptor: ConfirmOderFragmentAdaptor


    private val confirmOrderViewModel: ConfirmOrderFragmentViewModel by viewModels()
    private val viewModel: CostDashBoardViewModel by viewModels()
    private val printBillViewModel: PrintViewModel by viewModels()

    private var isPrinterConnected: Boolean = false
    private lateinit var callback: ItemTouchHelper.SimpleCallback
    private val args: BillingFragmentArgs by navArgs()
    private val arrItem = mutableListOf<ItemMasterFoodItem>()
    private val customDiningRequest: ConfirmDiningRequest =
        ConfirmDiningRequest(ConfirmDiningBody())
    private var receiptNo: String? = null
    private var confirmBillingRequest: ConfirmBillingRequest? = null


    private var isOptionMnuVisible: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor(R.color.semi_white_color_two)
        binding = BillingFragmentLayoutBinding.bind(view)
        binding.qrCodeScan.setOnClickListener {
            val action = BillingFragmentDirections.actionGlobalScanQrCodeFragment(
                Url_Text,
                null,
                FoodItemList(arrItem),
                customDiningRequest,
                WhereToGoFromScan.BILLPAYMENT.name
            )
            findNavController().navigate(action)
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


        //Check Bill Status
        getPrintConnectResponse()
        getCheckBillResponse()
        getPrintInvoiceResponse()
        getBillPrintResponse()
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
                FoodItemList(arrItem), null, customDiningRequest
            ,WhereToGoFromSearch.BILLPAYMENT.name
            )
            findNavController().navigate(action)
        }

        binding.infoBtn.setOnClickListener {
            //Show Swipe dialog
            activity?.dialogOption(listOf("About User", "Help"), this)
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
                WhereToGoFromSearch.BILLPAYMENT.name
            )
            findNavController().navigate(action)
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



    private fun getBillPrintResponse() {
        printBillViewModel.doPrinting.observe(viewLifecycleOwner) {
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
                    showDialogBox(
                        "Success",
                        "${it.data}",
                        icon = R.drawable.ic_success,
                        isCancel = false
                    ) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun getPrintInvoiceResponse() {
        viewModel.printBillInvoice.observe(viewLifecycleOwner){
            when(it){
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
                    (it.data as PrintInvoice?)?.let { printInvoice ->
                        Log.i("PRINT_INVOICE", "getPrintInvoiceResponse: $printInvoice")
                    printBillViewModel.doPrintInvoice(printInvoice)
                    }?:run{
                        showDialogBox(
                            "Success", "Invoice Generate Successfully", icon = R.drawable.ic_success, isCancel = false
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
            when (it) {
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
                    val billObj = confirmBillingRequest?.body!!
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


    private fun getCheckBillResponse() {
        viewModel.checkBillingStatus.observe(viewLifecycleOwner) {
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
                is ApisResponse.Loading -> showPb("${it.data}")
                is ApisResponse.Success -> {
                    hidePb()
                    if (it.data is String){
                        showDialogBox(
                            "Success", it.data, icon = R.drawable.ic_success, isCancel = false
                        ) {
                            findNavController().popBackStack()
                        }
                    }else{
                        val pair=it.data as Pair<*,*>
                        viewModel.getPrintBillInvoiceResponse(PrintInvoiceRequest(
                            PrintInvoiceRequestBody(pair.first as String)
                        ))
                        activity?.msg("Print Invoice")
                    }
                }
            }
        }
    }


    private fun getSendBillToEdcResponse() {
        viewModel.sendBillingToEdc.observe(viewLifecycleOwner) {
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
                is ApisResponse.Loading -> showPb("${it.data}")
                is ApisResponse.Success -> {
                    hidePb()
                    arrItem.clear()
                    confirmOrderViewModel.getOrderList(null)
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
                    itemClickListerForUpdate = { res ->
                        updateQtyDialogBox(res)
                    },
                    itemClickInstructionLinter = { res ->
                        updateFreeTxt(res)
                    },
                    itemClickAmountLinter = { res ->
                        updateAmount(res)
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
            binding.foodMnuBtn.show()
            binding.checkStatusIc.show()
            binding.foodMnuBtn.animation = enterAnim
            binding.checkStatusIc.animation = enterAnim
        } ?: run {
            binding.foodMnuBtn.show()
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
            uOM = barcode.uOM,
            decimalAllowed = barcode.decimalAllowed
        )
        itemMaster.foodQty = barcode.qty.toDouble()
        val amt =
            (ListOfFoodItemToSearchAdaptor.setPrice(itemMaster.salePrice) * itemMaster.foodQty)
        itemMaster.foodAmt = "%.4f".format(amt).toDouble()
        val mutableList = mutableListOf<ItemMasterFoodItem>()
        if (arrItem.isNotEmpty()) {
            mutableList.addAll(arrItem)
        }
        mutableList.add(ItemMasterFoodItem(itemMaster, itemMaster.foodQty, itemMaster.foodAmt))
        confirmOrderViewModel.getOrderList(FoodItemList(mutableList))
        activity?.msg(itemMaster.itemName + "\n${getEmojiByUnicode(0x2705)}")
    }
}
