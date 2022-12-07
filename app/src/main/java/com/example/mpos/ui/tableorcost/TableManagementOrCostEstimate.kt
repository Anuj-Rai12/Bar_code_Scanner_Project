package com.example.mpos.ui.tableorcost

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mpos.R
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
    private val args: TableManagementOrCostEstimateArgs by navArgs()
    private val viewModel: LoginScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.changeStatusBarColor()
        binding = TableOrCostLayoutBinding.bind(view)
        binding.mposId3.text = args.information.storeName
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
                SHOWROOMESTIMATE -> {
                    list.add(SelectionDataClass.showRoom)
                }
                RESTAURANTESTIMATE -> {
                    list.add(SelectionDataClass.restaurant)
                }
                SHOWROOMBILLING -> {
                    list.add(SelectionDataClass.showroomBilling)
                }
                RESTAURANTBILLING -> {
                    list.add(SelectionDataClass.restaurantBilling)
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
            "Failed!!",
            desc,
            "Ok",
            icon = R.drawable.ic_error
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
                    TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToBillingFragment(
                        null,
                        null
                    )
                }
                SHOWROOMESTIMATE -> {
                    TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToShowRoomEstimationFragment(
                        null,
                        null
                    )
                }
                RESTAURANTESTIMATE -> {
                    TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToRestaurantEstimationFragments(
                        null,
                        null
                    )
                }
                //Need to do Work
                SHOWROOMBILLING -> {
                    TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToShowRoomBillingFragment(
                        null,
                        null
                    )
                }
                RESTAURANTBILLING -> {
                    TableManagementOrCostEstimateDirections.actionTableManagementOrCostEstimateToRestaurantBillingFragment(
                        null,
                        null
                    )
                }
            }
        RestaurantSingletonCls.getInstance().getScreenType()?.let {
            findNavController().safeNavigate(action)
        }?:binding.root.showSandbar("Cannot Navigate to Screen")
    }

}