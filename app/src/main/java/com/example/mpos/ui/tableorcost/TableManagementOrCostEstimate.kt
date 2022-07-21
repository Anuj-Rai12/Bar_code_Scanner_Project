package com.example.mpos.ui.tableorcost

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mpos.R
import com.example.mpos.databinding.TableOrCostLayoutBinding
import com.example.mpos.ui.tableorcost.adaptor.TableManagementOrCostRecyclerAdaptor
import com.example.mpos.ui.tableorcost.model.SelectionDataClass
import com.example.mpos.utils.*


class TableManagementOrCostEstimate : Fragment(R.layout.table_or_cost_layout) {

    private lateinit var binding: TableOrCostLayoutBinding
    private lateinit var tableManagementOrCostRecyclerAdaptor: TableManagementOrCostRecyclerAdaptor
    private val args: TableManagementOrCostEstimateArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor()
        binding = TableOrCostLayoutBinding.bind(view)
        binding.mposId3.text = args.storeName
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
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            tableManagementOrCostRecyclerAdaptor = TableManagementOrCostRecyclerAdaptor {
                Log.i(TAG, "setRecycleView: $it")
                val action = if (it.title == getString(R.string.item_table)) {
                    TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToTableManagementFragment(
                        args.storeName
                    )
                } else {
                    TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToCostDashBoardFragment(
                        null,
                        null
                    )
                }
                findNavController().navigate(action)
            }
            adapter = tableManagementOrCostRecyclerAdaptor
        }
    }

    private fun setData() {
        val list = listOf(
            SelectionDataClass(
                image = R.drawable.ic_waiter,
                title = getString(R.string.item_table)
            ),
            SelectionDataClass(
                image = R.drawable.ic_cost_estimation,
                title = getString(R.string.item_cost)
            )
        )
        tableManagementOrCostRecyclerAdaptor.submitList(list)
    }
}