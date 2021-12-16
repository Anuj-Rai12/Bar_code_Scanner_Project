package com.example.offiqlresturantapp.ui.tableorcost

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.TableOrCostLayoutBinding
import com.example.offiqlresturantapp.ui.tableorcost.adaptor.TableManagementOrCostRecyclerAdaptor
import com.example.offiqlresturantapp.ui.tableorcost.model.SelectionDataClass
import com.example.offiqlresturantapp.utils.TAG
import com.example.offiqlresturantapp.utils.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TableManagementOrCostEstimate : Fragment(R.layout.table_or_cost_layout) {

    private lateinit var binding: TableOrCostLayoutBinding
    private lateinit var tableManagementOrCostRecyclerAdaptor: TableManagementOrCostRecyclerAdaptor
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor()
        binding = TableOrCostLayoutBinding.bind(view)
        setRecycleView()
        setData()
    }

    private fun setRecycleView() {
        binding.chooseTableOrCostRecycle.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            tableManagementOrCostRecyclerAdaptor = TableManagementOrCostRecyclerAdaptor {
                Log.i(TAG, "setRecycleView: $it")
                val action =
                    TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToTableManagementFragment()
                findNavController().navigate(action)
            }
            adapter = tableManagementOrCostRecyclerAdaptor
        }
    }

    private fun setData() {
        val list = listOf(
            SelectionDataClass(
                image = R.drawable.tablewaiter,
                title = getString(R.string.item_table)
            ),
            SelectionDataClass(
                image = R.drawable.costestimiation,
                title = getString(R.string.item_cost)
            )
        )
        tableManagementOrCostRecyclerAdaptor.submitList(list)
    }
}