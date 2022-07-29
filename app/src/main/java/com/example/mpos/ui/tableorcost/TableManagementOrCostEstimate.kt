package com.example.mpos.ui.tableorcost

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mpos.R
import com.example.mpos.databinding.TableOrCostLayoutBinding
import com.example.mpos.ui.tableorcost.adaptor.TableManagementOrCostRecyclerAdaptor
import com.example.mpos.ui.tableorcost.model.SelectionDataClass
import com.example.mpos.ui.tableorcost.model.SelectionDataClass.Companion.RestaurantSelection.*
import com.example.mpos.utils.TAG
import com.example.mpos.utils.changeStatusBarColor
import com.example.mpos.utils.showDialogBox
import java.util.*


class TableManagementOrCostEstimate : Fragment(R.layout.table_or_cost_layout) {

    private lateinit var binding: TableOrCostLayoutBinding
    private lateinit var tableManagementOrCostRecyclerAdaptor: TableManagementOrCostRecyclerAdaptor
    private val args: TableManagementOrCostEstimateArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor()
        binding = TableOrCostLayoutBinding.bind(view)
        binding.mposId3.text = args.information.storeName
        setRecycleView()
        setData()
        //removeItemFromBackStack()
        //showCountOfBackStack()
        binding.logoutBtnIc2.setOnClickListener {
            showDialog()
        }
        binding.logoutTxt2.setOnClickListener {
            showDialog()
        }
    }

    private fun setData() {
        val list = mutableListOf<SelectionDataClass>()
        args.information.screenList.forEach { item ->
            val value = item.trim().uppercase(Locale.getDefault()).replace("\\s".toRegex(), "")
            Log.i("ITEMS", "setData: $value")
            when (valueOf(value)) {
                TABLEMGT -> {
                    list.add(SelectionDataClass.tblManagement)
                }
                TABLERESERVATION -> {
                    list.add(SelectionDataClass.tblReservation)
                }
                ESTIMATION -> {
                    list.add(SelectionDataClass.cost)
                }
                BILLING -> {
                    list.add(SelectionDataClass.bill)
                }
            }
        }
        tableManagementOrCostRecyclerAdaptor.submitList(list)
    }

    private fun showDialog() {
        showDialogBox(
            "LogOut!!",
            "Are you Sure You want to Logout?",
            "Yes",
            icon = R.drawable.ic_logout,
            cancel = "No"
        ) {
            (activity as com.example.mpos.MainActivity?)?.logout()
        }
    }

    private fun setRecycleView() {
        binding.chooseTableOrCostRecycle.apply {
            setHasFixedSize(true)
            tableManagementOrCostRecyclerAdaptor = TableManagementOrCostRecyclerAdaptor {
                Log.i(TAG, "setRecycleView: $it")
                screenNav(it)
            }
            adapter = tableManagementOrCostRecyclerAdaptor
        }
    }

    private fun screenNav(selection: SelectionDataClass) {
        val action =
            when (valueOf(selection.type)) {
                TABLEMGT -> {
                    TableManagementOrCostEstimateDirections
                        .actionTableManagementOrCostEstimateToTableManagementFragment(args.information.storeName)
                }
                ESTIMATION -> {
                    TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToCostDashBoardFragment(
                        null,
                        null
                    )
                }
                TABLERESERVATION -> {
                    TableManagementOrCostEstimateDirections
                        .actionTableManagementOrCostEstimateToTableReservationFragment()
                }
                BILLING -> {
                    TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToBillingFragment()
                }
            }
        findNavController().navigate(action)
    }

}