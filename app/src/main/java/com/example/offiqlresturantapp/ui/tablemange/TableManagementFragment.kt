package com.example.offiqlresturantapp.ui.tablemange

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.databinding.TableMangmentLayoutBinding
import com.example.offiqlresturantapp.ui.tablemange.adaptor.TableManagementAdaptor
import com.example.offiqlresturantapp.ui.tablemange.model.TableData
import com.example.offiqlresturantapp.utils.TAG
import com.example.offiqlresturantapp.utils.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TableManagementFragment : Fragment(R.layout.table_mangment_layout) {
    private lateinit var binding: TableMangmentLayoutBinding
    private lateinit var tableManagementAdaptor: TableManagementAdaptor

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color)
        binding = TableMangmentLayoutBinding.bind(view)
        binding.tableInfoDetail.text = "Area Code: A102 - Outdoor"
        setRecycleView()
        setUpData()
    }

    private fun setUpData() {
        val list = listOf(
            TableData(
                msg = getString(R.string.open_table),
                tbl = "TBL - 1"
            ),
            TableData(
                isOpen = false,
                isOccupied = true,
                totalPeople = 4,
                totalTime = 1,
                msg = null,
                tbl = "TBL - 2"
            ),
            TableData(
                isOpen = false,
                msg = "Reserved\n3:00PM",
                tbl = "TBL - 3",
                isBooked = true
            ),
            TableData(
                msg = getString(R.string.open_table),
                tbl = "TBL - 4"
            ),
            TableData(
                isOpen = false,
                isOccupied = true,
                totalPeople = 3,
                totalTime = 2,
                msg = null,
                tbl = "TBL - 5"
            ),
            TableData(
                isOpen = false,
                msg = "Reserved\n6:00PM",
                tbl = "TBL - 6",
                isBooked = true
            ),
            TableData(
                msg = getString(R.string.open_table),
                tbl = "TBL - 7"
            ),
            TableData(
                isOccupied = true,
                totalPeople = 1,
                totalTime = 3,
                msg = null,
                isOpen = false,
                tbl = "TBL - 8"

            ),
            TableData(
                msg = "Reserved\n9:00PM",
                tbl = "TBL - 9",
                isOpen = false,
                isBooked = true
            ),
        )

        tableManagementAdaptor.submitList(list)

    }

    private fun setRecycleView() {
        binding.totalTableRecycler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireActivity(), 2)
            tableManagementAdaptor = TableManagementAdaptor {
                Log.i(TAG, "setRecycleView: $it")
                val action=TableManagementFragmentDirections.actionTableManagementFragmentToConfirmOderFragment()
                findNavController().navigate(action)
            }
            adapter = tableManagementAdaptor
        }
    }
}