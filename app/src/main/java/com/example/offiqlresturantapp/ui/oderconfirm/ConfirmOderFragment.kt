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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.ConfirmOrderLayoutBinding
import com.example.offiqlresturantapp.ui.oderconfirm.adaptor.ConfirmOderFragmentAdaptor
import com.example.offiqlresturantapp.ui.searchfood.model.FoodItem
import com.example.offiqlresturantapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

@AndroidEntryPoint
class ConfirmOderFragment : Fragment(R.layout.confirm_order_layout) {
    private lateinit var binding: ConfirmOrderLayoutBinding
    private lateinit var confirmOderFragmentAdaptor: ConfirmOderFragmentAdaptor
    private var list = mutableListOf<FoodItem>()
    private var flagForViewDeals: Boolean = false
    private var listOfSelectedFoodItemForViewDeals = mutableListOf<FoodItem>()
    private lateinit var callback: ItemTouchHelper.SimpleCallback
    private val args: ConfirmOderFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color)
        binding = ConfirmOrderLayoutBinding.bind(view)
        binding.qrCodeScan.setOnClickListener {
            requireActivity().msg(getString(R.string.scan_btn))
        }
        setRecycle()
        setCallBack()
        //Temporal Showing Data
        binding.listOfItemRecycleView.show()
        binding.orderRecycleViewHint.hide()
        if (args.list == null || args.list?.foodList.isNullOrEmpty()) {
            setData()
        } else {
            var getotal = 0
            args.list?.foodList?.forEach { it ->
                getotal += it.foodAmt
                list.add(it)
            }
            if (!list.isNullOrEmpty()) {
                confirmOderFragmentAdaptor.submitList(list)
                binding.totalOrderAmt.text = "$Rs_Symbol $getotal"
            }
        }
        binding.viewOfferBtn.setOnClickListener {
            if (!list.isNullOrEmpty() && listOfSelectedFoodItemForViewDeals.isNullOrEmpty() && !flagForViewDeals) {
                setRecycle(true)
                flagForViewDeals = true
                setData()
            } else if (!listOfSelectedFoodItemForViewDeals.isNullOrEmpty()) {
                Log.i(TAG, "onViewCreated: $listOfSelectedFoodItemForViewDeals")
            } else {
                setRecycle()
                setData()
                flagForViewDeals=false
            }
        }

        binding.restItemBtn.setOnClickListener {
            binding.orderRecycleViewHint.show()
            binding.listOfItemRecycleView.hide()
            list.clear()
            binding.totalOrderAmt.text = "$Rs_Symbol 000"
        }

        binding.searchBoxTxt.setOnClickListener {
            //New Fragment
            val action =
                ConfirmOderFragmentDirections.actionConfirmOderFragmentToSearchFoodFragment()
            findNavController().navigate(action)
        }
    }

    private fun setData() {
        list = mutableListOf(
            FoodItem(
                foodName = "Chloe Bhature",
                foodAmt = 125,
                foodPrice = 125,
                foodQTY = 1,
                offerDesc = null,
                foodOffer = null,
            ),
            FoodItem(
                foodName = "Chloe Samosa Chart",
                foodAmt = 13,
                foodPrice = 13,
                foodQTY = 2,
                offerDesc = null,
                foodOffer = null,
            ),
            FoodItem(
                foodName = "Chloe Kulcha",
                foodAmt = 15,
                foodPrice = 15,
                foodQTY = 3,
                offerDesc = null,
                foodOffer = null,
            ),
            FoodItem(
                foodName = "Chloe (Bowl)",
                foodAmt = 135,
                foodPrice = 135,
                foodQTY = 4,
                offerDesc = null,
                foodOffer = null,
            )
        )

        confirmOderFragmentAdaptor.submitList(list)
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
                    list.remove(it)
                    confirmOderFragmentAdaptor.submitList(list)
                    confirmOderFragmentAdaptor.notifyDataSetChanged()
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


    private fun setRecycle(flag: Boolean = false) {
        binding.listOfItemRecycleView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            confirmOderFragmentAdaptor = ConfirmOderFragmentAdaptor({
                if (listOfSelectedFoodItemForViewDeals.contains(it)) {
                    listOfSelectedFoodItemForViewDeals.remove(it)
                } else {
                    listOfSelectedFoodItemForViewDeals.add(it)
                }
                Log.i(TAG, "setRecycle: $listOfSelectedFoodItemForViewDeals")
            }, {
                return@ConfirmOderFragmentAdaptor flag
            })
            adapter = confirmOderFragmentAdaptor
        }
    }
}