package com.example.mpos.ui.cost

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
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
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.databinding.CostCalDashbordLayoutBinding
import com.example.mpos.ui.menu.bottomsheet.MenuBottomSheetFragment
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.oderconfirm.ConfirmOderFragmentDirections
import com.example.mpos.ui.oderconfirm.adaptor.ConfirmOderFragmentAdaptor
import com.example.mpos.ui.oderconfirm.view_model.ConfirmOrderFragmentViewModel
import com.example.mpos.ui.searchfood.adaptor.ListOfFoodItemToSearchAdaptor
import com.example.mpos.ui.searchfood.model.FoodItemList
import com.example.mpos.ui.searchfood.model.ItemMasterFoodItem
import com.example.mpos.utils.*
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class CostDashBoardFragment : Fragment(R.layout.cost_cal_dashbord_layout),
    OnBottomSheetClickListener {
    private lateinit var binding: CostCalDashbordLayoutBinding

    private lateinit var confirmOderFragmentAdaptor: ConfirmOderFragmentAdaptor
    private val viewModel: ConfirmOrderFragmentViewModel by viewModels()
    private var flagForViewDeals: Boolean = false
    private lateinit var callback: ItemTouchHelper.SimpleCallback
    private val args: CostDashBoardFragmentArgs by navArgs()
    private val arrItem = mutableListOf<ItemMasterFoodItem>()
    private var receiptNo: String? = null
    private var customDiningRequest: ConfirmDiningRequest? = null
    private var isCustomerDiningRequestVisible: Boolean = true
    private var isOrderIsVisible = false


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

        viewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { map ->
                if (!map.values.first()) {
                    showSnackBar(map.keys.first(), R.color.color_red)
                } else {
                    showSnackBar(map.keys.first(), R.color.green_color)
                }
            }
        }

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

        binding.confirmOrderBtn.setOnClickListener {
            activity?.msg("Working")
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
            viewModel.getGrandTotal(null)
            viewModel.removeItemFromListOrder()
            initial()
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

    private fun setInitialValue() {
        val list = mutableListOf<ItemMasterFoodItem>()
        if (args.list != null) {
            if (arrItem.isNotEmpty()) {
                if (!args.list?.foodList?.containsAll(arrItem)!!) {
                    list.addAll(arrItem)
                }
            }
            list.addAll(args.list?.foodList!!)
            viewModel.getOrderList(FoodItemList(list))
        } else if (args.list == null && arrItem.isNotEmpty()) {
            list.addAll(arrItem)
            viewModel.getOrderList(FoodItemList(list))
        } else if (args.list == null && list.isEmpty()) {
            viewModel.getOrderList(null)
        }
    }

    private fun getGrandTotal() {
        viewModel.grandTotal.observe(viewLifecycleOwner) {
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
                        viewModel.getOrderItem(it)
                }, itemClickListerForUpdate = { res ->
                    updateQtyDialogBox(res)
                })
            adapter = confirmOderFragmentAdaptor
        }
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

    /*private fun oopsSomeThingWentWrong() {
        showSnackBar(msg = "Oops Something Went Wrong..", color = R.color.color_red)
    }

    private fun confirmDinningOrder(confirmDiningRequest: ConfirmDiningRequest) {
        Log.i("confirmDinningOrder", "$confirmDiningRequest")
        viewModel.updateAndLockTbl(confirmDiningRequest)
    }

    private fun confirmOrder(confirmOrderRequest: ConfirmOrderRequest) {
        Log.i("confirmOrder", "$confirmOrderRequest")
        viewModel.saveUserOrderItem(confirmOrderRequest)
    }*/

    @SuppressLint("SetTextI18n")
    private fun initial() {
        binding.orderRecycleViewHint.show()
        binding.listOfItemRecycleView.hide()
    }

    private fun updateQtyDialogBox(itemMasterFoodItem: ItemMasterFoodItem) {
        showQtyDialog(true, itemMasterFoodItem.itemMaster, cancel = {}, res = {
            viewModel.addUpdateQty(
                food = ItemMasterFoodItem(it, it.foodQty, it.foodAmt),
                itemRemoved = itemMasterFoodItem
            )
        })
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
        viewModel.getOrderList(FoodItemList(mutableList))
        activity?.msg(itemMaster.itemName + "\n${getEmojiByUnicode(0x2705)}")
    }
}