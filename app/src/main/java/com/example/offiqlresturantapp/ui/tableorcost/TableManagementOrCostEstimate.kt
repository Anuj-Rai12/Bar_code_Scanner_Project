package com.example.offiqlresturantapp.ui.tableorcost

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.TableOrCostLayoutBinding
import com.example.offiqlresturantapp.ui.tableorcost.adaptor.TableManagementOrCostRecyclerAdaptor
import com.example.offiqlresturantapp.ui.tableorcost.model.SelectionDataClass
import com.example.offiqlresturantapp.utils.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TableManagementOrCostEstimate : Fragment(R.layout.table_or_cost_layout) {

    private lateinit var binding: TableOrCostLayoutBinding
    private lateinit var tableManagementOrCostRecyclerAdaptor: TableManagementOrCostRecyclerAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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