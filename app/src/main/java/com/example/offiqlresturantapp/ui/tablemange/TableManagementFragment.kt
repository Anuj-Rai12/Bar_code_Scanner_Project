package com.example.offiqlresturantapp.ui.tablemange

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.offiqlresturantapp.R
import com.example.offiqlresturantapp.data.table_info.model.json.TableInformationJsonResponse
import com.example.offiqlresturantapp.databinding.TableMangmentLayoutBinding
import com.example.offiqlresturantapp.ui.tablemange.adaptor.TableManagementAdaptor
import com.example.offiqlresturantapp.ui.tablemange.view_model.TableManagementViewModel
import com.example.offiqlresturantapp.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TableManagementFragment : Fragment(R.layout.table_mangment_layout) {
    private lateinit var binding: TableMangmentLayoutBinding
    private lateinit var tableManagementAdaptor: TableManagementAdaptor
    private val args: TableManagementFragmentArgs by navArgs()
    private val viewModel: TableManagementViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color)
        binding = TableMangmentLayoutBinding.bind(view)
        binding.tableInfoDetail.text = args.storeName
        viewModel.events.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { str ->
                binding.root.showSandbar(
                    str,
                    Snackbar.LENGTH_INDEFINITE,
                    requireActivity().getColorInt(R.color.color_red)
                )
            }
        }
        setRecycleView()
        setUpData()
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpData() {
        viewModel.tblInfo.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> Log.i(TAG, "setUpData: ${it.exception?.localizedMessage}")
                is ApisResponse.Loading -> Log.i(TAG, "setUpData: ${it.data.toString()}")
                is ApisResponse.Success -> {
                    it.data?.let {cls->
                        (cls as TableInformationJsonResponse?)?.let { res ->
                            binding.tableInfoDetail.append("\nTotal Table Count ${res.totalTableCount},")
                            tableManagementAdaptor.notifyDataSetChanged()
                            tableManagementAdaptor.submitList(res.tableDetails)
                        } ?: binding.root.showSandbar(
                            "Error the Get Data",
                            Snackbar.LENGTH_LONG,
                            requireActivity().getColorInt(R.color.color_red)
                        )
                    }
                }
            }
        }
    }

    private fun setRecycleView() {
        binding.totalTableRecycler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireActivity(), 2)
            tableManagementAdaptor = TableManagementAdaptor {
                Log.i(TAG, "setRecycleView: $it")
                val action =
                    TableManagementFragmentDirections.actionTableManagementFragmentToConfirmOderFragment(
                        null
                    )
                findNavController().navigate(action)
            }
            adapter = tableManagementAdaptor
        }
    }
}