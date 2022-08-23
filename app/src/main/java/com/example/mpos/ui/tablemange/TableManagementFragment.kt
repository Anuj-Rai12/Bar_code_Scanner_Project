package com.example.mpos.ui.tablemange

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mpos.R
import com.example.mpos.data.table_info.model.json.TableDetail
import com.example.mpos.databinding.TableMangmentLayoutBinding
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.tablemange.adaptor.TableManagementAdaptor
import com.example.mpos.ui.tablemange.view_model.TableManagementViewModel
import com.example.mpos.utils.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TableManagementFragment : Fragment(R.layout.table_mangment_layout),
    OnBottomSheetClickListener {
    private lateinit var binding: TableMangmentLayoutBinding
    private lateinit var tableManagementAdaptor: TableManagementAdaptor
    private val args: TableManagementFragmentArgs by navArgs()
    private val viewModel: TableManagementViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color_two)
        binding = TableMangmentLayoutBinding.bind(view)
        binding.tableInfoDetail.text = args.storeName
        viewModel.events.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { str ->
                binding.root.showSandbar(
                    str,
                    Snackbar.LENGTH_INDEFINITE,
                    requireActivity().getColorInt(R.color.color_red)
                ) {
                    return@showSandbar "OK"
                }
            }
        }
        setRecycleView()
        setUpData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpData() {
        viewModel.tblInfo.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    hideOrShowProgress(null)
                    val exp = it.exception?.localizedMessage
                    val error = exp?.let {
                        return@let "Failed to get Response Error Details :-\n$exp"
                    } ?: "Failed to get Response Error Detail :-\nUnknown  Error"

                    showDialogBox("Failed!!", error, icon = R.drawable.ic_error) {}
                    Log.i(TAG, "setUpData: $exp")
                }
                is ApisResponse.Loading -> {
                    val res = it.data as List<*>?
                    if (res.isNullOrEmpty()) {
                        hideOrShowProgress("Loading Tables")
                    } else {
                        displayData(it.data)
                    }
                }
                is ApisResponse.Success -> {
                    hideOrShowProgress(null)
                    displayData(it.data)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun displayData(data: Any?) {
        data?.let { cls ->
            (cls as List<TableDetail>).let { res ->
                if (res.isNotEmpty()) {
                    lifecycleScope.launch {
                        delay(800)
                        binding.tableInfoDetail.append("\nTotal Table Count ${res.size},")
                        tableManagementAdaptor.notifyDataSetChanged()
                        tableManagementAdaptor.submitList(res)
                    }
                } else {
                    binding.root.showSandbar(
                        "Error the Get Data",
                        Snackbar.LENGTH_LONG,
                        requireActivity().getColorInt(R.color.color_red)
                    )
                }
            }
        }
    }

    private fun hideOrShowProgress(msg: String?) {
        msg?.let {
            binding.pbLayout.root.show()
            binding.pbLayout.titleTxt.text = it
            return@let
        } ?: binding.pbLayout.root.hide()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchTbl()
    }

    private fun setRecycleView() {
        binding.totalTableRecycler.apply {
            setHasFixedSize(true)
            tableManagementAdaptor = TableManagementAdaptor()
            tableManagementAdaptor.onClickLister=this@TableManagementFragment
            adapter = tableManagementAdaptor
        }
    }

    override fun <T> onItemClicked(response: T) {
        val res= response as TableDetail
        Log.i(TAG, "setRecycleView: $res")
        if (res.billPrinted.equals("No", true)) {
            val action = TableManagementFragmentDirections
                .actionTableManagementFragmentToConfirmOderFragment(null, res, null)
            findNavController().navigate(action)
        } else {
            activity?.msg("Cannot Open it already printed ${getEmojiByUnicode(0x1F5A8)}")
            Handler(Looper.getMainLooper()).post {
                Log.i(TAG, "setRecycleView: dialog hit")
                showDialogBox(
                    "Cannot Access",
                    "Not Allowed to take order in this Table, because table bill is already Printed ${
                        getEmojiByUnicode(
                            0x1F5A8
                        )
                    }\n" +
                            "Try again when table is open"
                ) {}
            }
        }
    }
}