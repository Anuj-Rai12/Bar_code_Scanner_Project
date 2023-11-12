package com.example.mpos.ui.tableorcost

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mpos.R
import com.example.mpos.data.login.model.api.json.ApkLoginJsonResponse
import com.example.mpos.databinding.TableOrCostLayoutBinding
import com.example.mpos.ui.login.viewmodel.LoginScreenViewModel
import com.example.mpos.ui.tableorcost.adaptor.TableManagementOrCostRecyclerAdaptor
import com.example.mpos.ui.tableorcost.model.SelectionDataClass
import com.example.mpos.ui.tableorcost.model.SelectionDataClass.Companion.RestaurantSelection.*
import com.example.mpos.utils.*
import java.util.*


class TableManagementOrCostEstimate : Fragment(R.layout.table_or_cost_layout) {

    private lateinit var binding: TableOrCostLayoutBinding
    private lateinit var tableManagementOrCostRecyclerAdaptor: TableManagementOrCostRecyclerAdaptor
    private val args by lazy {
        arguments?.getParcelable<ApkLoginJsonResponse>("TBL_VALUE")
            ?: activity?.intent?.getParcelableExtra("TBL_VALUE")
    }
    private val viewModel: LoginScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor()
        binding = TableOrCostLayoutBinding.bind(view)
        binding.mposId3.text = args?.storeName
        setRecycleView()
        setData()
        getLogOutResponse()
        viewModel.events.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { msg ->
                showErrorDialog(msg)
            }
        }
        binding.logoutBtnIc2.setOnClickListener {
            showDialog()
        }
        binding.logoutTxt2.setOnClickListener {
            showDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        DealsStoreInstance.getInstance().setIsResetButtonClick(false)
    }

    private fun getLogOutResponse() {
        viewModel.logOutStaff.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    binding.pbLayout.root.hide()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { msg ->
                            showErrorDialog(msg)
                        }
                    } else {
                        showErrorDialog(it.data)
                    }
                }
                is ApisResponse.Loading -> {
                    binding.pbLayout.titleTxt.text = it.data
                    binding.pbLayout.root.show()
                }
                is ApisResponse.Success -> {
                    binding.pbLayout.root.hide()
                    (activity as com.example.mpos.MainActivity?)?.logout()
                    showDialogBox(
                        "LogOut!!",
                        "You Are No Log In!!\nPlease Proceed to Login Section",
                        isCancel = false,
                        icon = R.drawable.ic_logout
                    ) {
                        activity?.finish()
                    }
                }
            }
        }
    }

    private fun setData() {
        val list = mutableListOf<SelectionDataClass>()
        args?.screenList?.forEach { item ->
            val value =
                item.screenList.trim().uppercase(Locale.getDefault()).replace("\\s".toRegex(), "")
            Log.i("ITEMS", "setData: $value")
            when (valueOf(value)) {
                TABLEMGT -> {
                    list.add(
                        SelectionDataClass.generateData(
                            SelectionDataClass.TABLE_MGT,
                            R.drawable.ic_waiter,
                            TABLEMGT.name,
                            item.dynamicMenuEnable,
                            args?.itemScanWithBarcode!!,
                            item.uPICode,
                            item.billingFromEDC,
                            item.paymentLs,
                            args!!,
                            kotPrintFromEDC = item.kotPrintFromEDC,
                            enableCustDetail = item.enableCustDetail,
                            modernSearch = item.modernSearch,
                            estimatePrint = item.estimatePrint,
                            estimatePrintcount = item.estimatePrintCount
                        )
                    )
                }
                TABLERESERVATION -> {
                    list.add(
                        SelectionDataClass.generateData(
                            SelectionDataClass.TABLERESERVATION,
                            R.drawable.ic_table_dinner,
                            TABLERESERVATION.name,
                            item.dynamicMenuEnable,
                            args?.itemScanWithBarcode!!,
                            item.uPICode,
                            item.billingFromEDC,
                            item.paymentLs,
                            args!!,
                            kotPrintFromEDC = item.kotPrintFromEDC,
                            enableCustDetail = item.enableCustDetail,
                            modernSearch = item.modernSearch,
                            estimatePrint = item.estimatePrint,
                            estimatePrintcount = item.estimatePrintCount
                        )
                    )
                }
                ESTIMATION -> {
                    list.add(
                        SelectionDataClass.generateData(
                            SelectionDataClass.ESTIMATION,
                            R.drawable.ic_cost_estimation,
                            ESTIMATION.name,
                            item.dynamicMenuEnable,
                            args?.itemScanWithBarcode!!,
                            item.uPICode,
                            item.billingFromEDC,
                            item.paymentLs,
                            args!!,
                            kotPrintFromEDC = item.kotPrintFromEDC,
                            enableCustDetail = item.enableCustDetail,
                            modernSearch = item.modernSearch,
                            estimatePrint = item.estimatePrint,
                            estimatePrintcount = item.estimatePrintCount
                        )
                    )
                }
                BILLING -> {
                    list.add(
                        SelectionDataClass.generateData(
                            SelectionDataClass.BILLING,
                            R.drawable.ic_receipt_bill,
                            BILLING.name,
                            item.dynamicMenuEnable,
                            args?.itemScanWithBarcode!!,
                            item.uPICode,
                            item.billingFromEDC,
                            item.paymentLs,
                            args!!,
                            kotPrintFromEDC = item.kotPrintFromEDC,
                            enableCustDetail = item.enableCustDetail,
                            modernSearch = item.modernSearch,
                            estimatePrint = item.estimatePrint,
                            estimatePrintcount = item.estimatePrintCount
                        )
                    )
                }
                SHOWROOMESTIMATE -> {
                    list.add(
                        SelectionDataClass.generateData(
                            SelectionDataClass.SHOWROOMESTIMATE,
                            R.drawable.showroom_estimation,
                            SHOWROOMESTIMATE.name,
                            item.dynamicMenuEnable,
                            args?.itemScanWithBarcode!!,
                            item.uPICode,
                            item.billingFromEDC,
                            item.paymentLs,
                            args!!,
                            kotPrintFromEDC = item.kotPrintFromEDC,
                            enableCustDetail = item.enableCustDetail,
                            modernSearch = item.modernSearch,
                            estimatePrint = item.estimatePrint,
                            estimatePrintcount = item.estimatePrintCount
                        )
                    )
                }
                RESTAURANTESTIMATE -> {
                    list.add(
                        SelectionDataClass.generateData(
                            SelectionDataClass.RESTAURANTESTIMATE,
                            R.drawable.restaurant_estimation,
                            RESTAURANTESTIMATE.name,
                            item.dynamicMenuEnable,
                            args?.itemScanWithBarcode!!,
                            item.uPICode,
                            item.billingFromEDC,
                            item.paymentLs,
                            args!!,
                            kotPrintFromEDC = item.kotPrintFromEDC,
                            enableCustDetail = item.enableCustDetail,
                            modernSearch = item.modernSearch,
                            estimatePrint = item.estimatePrint,
                            estimatePrintcount = item.estimatePrintCount
                        )
                    )
                }
                SHOWROOMBILLING -> {
                    list.add(
                        SelectionDataClass.generateData(
                            SelectionDataClass.SHOWROOMBILLING,
                            R.drawable.ic_receipt_bill,
                            SHOWROOMBILLING.name,
                            item.dynamicMenuEnable,
                            args?.itemScanWithBarcode!!,
                            item.uPICode,
                            item.billingFromEDC,
                            item.paymentLs,
                            args!!,
                            kotPrintFromEDC = item.kotPrintFromEDC,
                            enableCustDetail = item.enableCustDetail,
                            modernSearch = item.modernSearch,
                            estimatePrint = item.estimatePrint,
                            estimatePrintcount = item.estimatePrintCount
                        )
                    )
                }
                RESTAURANTBILLING -> {
                    list.add(
                        SelectionDataClass.generateData(
                            SelectionDataClass.RESTAURANTBILLING,
                            R.drawable.ic_receipt_bill,
                            RESTAURANTBILLING.name,
                            item.dynamicMenuEnable,
                            args?.itemScanWithBarcode!!,
                            item.uPICode,
                            item.billingFromEDC,
                            item.paymentLs,
                            args!!,
                            kotPrintFromEDC = item.kotPrintFromEDC,
                            enableCustDetail = item.enableCustDetail,
                            modernSearch = item.modernSearch,
                            estimatePrint = item.estimatePrint,
                            estimatePrintcount = item.estimatePrintCount
                        )
                    )
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
            viewModel.staffLogOut()
        }
    }


    private fun showErrorDialog(desc: String) {
        showDialogBox(
            "Failed!!", desc, "Ok", icon = R.drawable.ic_error
        ) {}
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
        RestaurantSingletonCls.getInstance().setScreenType(selection.type)
        val action = when (valueOf(selection.type)) {
            TABLEMGT -> {
                TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToTableManagementFragment(
                    args?.storeName!!, selection
                )
            }
            ESTIMATION -> {
                TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToCostDashBoardFragment(
                    null, null, selection
                )
            }
            TABLERESERVATION -> {
                TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToTableReservationFragment(
                    selection
                )
            }
            BILLING -> {
                TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToBillingFragment(
                    null, null, selection
                )
            }
            SHOWROOMESTIMATE -> {
                TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToShowRoomEstimationFragment(
                    null, null, selection
                )
            }
            RESTAURANTESTIMATE -> {
                TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToRestaurantEstimationFragments(
                    null, null, selection
                )
            }
            //Need to do Work
            SHOWROOMBILLING -> {
                TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToShowRoomBillingFragment(
                    null, null, selection
                )
            }
            RESTAURANTBILLING -> {
                TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToRestaurantBillingFragment(
                    null, null, selection
                )
            }
        }
        RestaurantSingletonCls.getInstance().getScreenType()?.let {
            findNavController().navigate(action)
        } ?: binding.root.showSandbar("Cannot Navigate to Screen")
    }

}