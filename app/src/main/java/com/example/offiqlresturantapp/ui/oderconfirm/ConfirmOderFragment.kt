package com.example.offiqlresturantapp.ui.oderconfirm

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
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.data.cofirmDining.ConfirmDiningRequest
import com.example.offiqlresturantapp.data.cofirmDining.response.ConfirmDiningSuccessResponse
import com.example.offiqlresturantapp.data.confirmOrder.ConfirmOrderBody
import com.example.offiqlresturantapp.data.confirmOrder.ConfirmOrderRequest
import com.example.offiqlresturantapp.data.confirmOrder.response.ConfirmOrderSuccessResponse
import com.example.offiqlresturantapp.databinding.ConfirmOrderLayoutBinding
import com.example.offiqlresturantapp.ui.oderconfirm.adaptor.ConfirmOderFragmentAdaptor
import com.example.offiqlresturantapp.ui.oderconfirm.view_model.ConfirmOrderFragmentViewModel
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItemList
import com.example.offiqlresturantapp.ui.searchfood.model.ItemMasterFoodItem
import com.example.offiqlresturantapp.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

@AndroidEntryPoint
class ConfirmOderFragment : Fragment(R.layout.confirm_order_layout) {
    private lateinit var binding: ConfirmOrderLayoutBinding
    private lateinit var confirmOderFragmentAdaptor: ConfirmOderFragmentAdaptor
    private val viewModel: ConfirmOrderFragmentViewModel by viewModels()
    private var flagForViewDeals: Boolean = false
    private lateinit var callback: ItemTouchHelper.SimpleCallback
    private val args: ConfirmOderFragmentArgs by navArgs()
    private val arrItem = mutableListOf<ItemMasterFoodItem>()
    private var receiptNo: Long = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color)
        binding = ConfirmOrderLayoutBinding.bind(view)
        binding.qrCodeScan.setOnClickListener {
            val action = ConfirmOderFragmentDirections
                .actionGlobalScanQrCodeFragment(Url_Text, args.tbl, FoodItemList(arrItem))
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
            binding.orderBookingTimeTxt.text = getString(R.string.sample_tbl_time, it)
        }
        viewModel.getOrderList(args.list)
        getData()
        getConfirmDinningResponse()
        getConfirmOrderResponse()

        binding.viewOfferBtn.setOnClickListener {
            if (!flagForViewDeals) {
                flagForViewDeals = true
                getViewDeals()
                confirmOderFragmentAdaptor.setCheckBoxType(flagForViewDeals)
            } else {
                flagForViewDeals = false
                confirmOderFragmentAdaptor.setCheckBoxType(flagForViewDeals)
            }
        }

        binding.restItemBtn.setOnClickListener {
            initial()
        }

        binding.searchBoxTxt.setOnClickListener {
            //New Fragment
            val action =
                ConfirmOderFragmentDirections.actionConfirmOderFragmentToSearchFoodFragment(args.tbl)
            findNavController().navigate(action)
        }


        binding.confirmOrderBtn.setOnClickListener {
            receiptNo = randomNumber(10000000)
            val singletonCls = RestaurantSingletonCls.getInstance()
            activity?.addDialogMaterial(
                title = "Receipt No: $receiptNo",
                time = viewModel.time.value ?: "04:00 PM",
                tableNo = args.tbl.tableNo,
                receiptNo = receiptNo,
                storeVar = singletonCls.getStoreId()!!,
                staffID = singletonCls.getUserId()!!
            ) { res ->
                confirmDinningOrder(res)
            }
        }

    }

    private fun getViewDeals() {
        viewModel.viewDeals.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                // Go To View Deals Fragments
                Log.i(TAG, "getViewDeals: ${it.size}")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        viewModel.listOfOrder.observe(viewLifecycleOwner) {
            binding.totalOrderAmt.text = viewModel.getGrandTotal(it.data)
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
                        binding.orderRecycleViewHint.hide()
                        binding.listOfItemRecycleView.show()
                        confirmOderFragmentAdaptor.notifyDataSetChanged()
                        confirmOderFragmentAdaptor.submitList(data)
                    }
                }
            }
        }
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
                    (it.data as ConfirmDiningSuccessResponse?)?.let { res ->
                        val error = res.body?.errorFound?.toBoolean()
                        val errorBdy = res.body?.errorText.toString()
                        if (error == true) {
                            val result =
                                Pair("Failed to Update Table", R.drawable.ic_error)
                            showDialogBox(result.first, errorBdy, icon = result.second)
                            binding.pbLayout.root.hide()
                        } else {
                            activity?.msg("create Receipt $receiptNo")
                            val confirmOrderRequest =
                                ConfirmOrderRequest(body = ConfirmOrderBody(receiptNo = receiptNo.toString()))
                            confirmOrder(confirmOrderRequest)
                        }
                    } ?: run {
                        oopsSomeThingWentWrong()
                        binding.pbLayout.root.hide()
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
                    binding.pbLayout.titleTxt.text = it.data.toString()
                }
                is ApisResponse.Success -> {
                    binding.pbLayout.root.hide()
                    (it.data as ConfirmOrderSuccessResponse?)?.let { res ->
                        val result = if (res.body?.returnValue == "01") {
                            Pair(R.drawable.ic_error,Pair("Failed to Insert!!", "Order is Not Inserted in Navision at All."))
                        } else {
                            Pair(R.drawable.ic_success,Pair("Successfully Inserted", "Order is Inserted in Navision at All."))
                        }
                        showDialogBox(result.second.first, result.second.second, icon = result.first)
                    } ?: run {
                        val res =
                            Pair("Failed to Insert!!", "Order is Not Inserted in Navision at All.")
                        showDialogBox(res.first, res.second, icon = R.drawable.ic_error)
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
            confirmOderFragmentAdaptor = ConfirmOderFragmentAdaptor {
                if (flagForViewDeals)
                    viewModel.getOrderItem(it)
            }
            adapter = confirmOderFragmentAdaptor
        }
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