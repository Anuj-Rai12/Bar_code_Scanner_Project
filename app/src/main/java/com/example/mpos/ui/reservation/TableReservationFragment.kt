package com.example.mpos.ui.reservation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.R
import com.example.mpos.data.reservation.request.AddReservationBody
import com.example.mpos.data.reservation.request.AddTableReservationRequest
import com.example.mpos.data.reservation.request.GetTableReservationRequest
import com.example.mpos.data.reservation.request.GetTableReservationRequestBody
import com.example.mpos.data.reservation.response.json.GetReservationResponse
import com.example.mpos.data.reservation.response.json.addRequest.AddReservationJsonResponse
import com.example.mpos.databinding.TableReservationLayoutBinding
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.reservation.adaptor.TblReservationAdaptor
import com.example.mpos.ui.reservation.viewmodel.TableReservationViewModel
import com.example.mpos.utils.*

class TableReservationFragment : Fragment(R.layout.table_reservation_layout),
    OnBottomSheetClickListener {
    private lateinit var binding: TableReservationLayoutBinding
    private lateinit var tblReservationAdaptor: TblReservationAdaptor

    private val viewModel: TableReservationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color_two)
        binding = TableReservationLayoutBinding.bind(view)

        viewModel.events.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { res ->
                showDialogResponse(res)
            }
        }

        setAdaptor()
        binding.bookTbl.extend()
        binding.bookTbl.setOnHoverListener { _, _ ->
            binding.bookTbl.extend()
            activity?.msg("add new Reservation")
            return@setOnHoverListener true
        }
        onScrollListener()
        getAllResponse()
        setData()
        makeReservationResponse()
        binding.bookTbl.setOnClickListener {
            viewModel.addReservation(
                AddTableReservationRequest(
                    AddReservationBody(
                        phoneNumber = "7894561230",
                        customerName = "Anuj Rai",
                        reservationDate = "2022-06-19",
                        reservationTime = "10:20 AM",
                        cover = "2",
                        reservationRemarks = "Testing API"
                    )
                )
            )
        }
    }

    private fun makeReservationResponse() {
        viewModel.addReservationItem.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    binding.pbLayout.root.hide()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { res ->
                            showDialogResponse(res)
                        }
                    } else {
                        showDialogResponse("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    binding.pbLayout.root.show()
                    binding.pbLayout.titleTxt.text = "${it.data}"
                }
                is ApisResponse.Success -> {
                    binding.pbLayout.root.hide()
                    val response = it.data as AddReservationJsonResponse
                    if (response.status) {
                        showDialogResponse(msg = response.message, title = "Success",icon= R.drawable.ic_success)
                    }else{
                        showDialogResponse(msg = response.message)
                    }
                }
            }
        }
    }

    private fun getAllResponse() {
        viewModel.getTableReservedInfo(
            GetTableReservationRequest(
                GetTableReservationRequestBody(
                    searchByMobile = "",
                    searchByName = ""
                )
            )
        )
    }

    private fun setData() {
        viewModel.getAllReservationItem.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    hidePbLayout()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { res ->
                            showDialogResponse(res)
                        }
                    } else {
                        showDialogResponse("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    showPbLayout("${it.data}")
                }
                is ApisResponse.Success -> {
                    hidePbLayout()
                    tblReservationAdaptor.notifyDataSetChanged()
                    tblReservationAdaptor.submitList(it.data as GetReservationResponse)
                }
            }
        }
    }

    private fun showDialogResponse(
        msg: String,
        title: String = "Failed",
        icon: Int = R.drawable.ic_error
    ) {
        showDialogBox(title, msg, icon = icon) {}
    }


    private fun showPbLayout(msg: String) {
        binding.pbLayout.root.show()
        binding.pbLayout.titleTxt.text = msg
        binding.timeReserveList.hide()
    }

    private fun hidePbLayout() {
        binding.pbLayout.root.hide()
        binding.timeReserveList.show()
    }

    private fun onScrollListener() {
        binding.timeReserveList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                if (position > 0) {
                    binding.bookTbl.shrink()
                } else {
                    binding.bookTbl.extend()
                }

            }
        })
    }

    private fun setAdaptor() {
        binding.timeReserveList.apply {
            tblReservationAdaptor = TblReservationAdaptor()
            tblReservationAdaptor.itemClickListener = this@TableReservationFragment
            adapter = tblReservationAdaptor
        }
    }

    override fun <T> onItemClicked(response: T) {
        activity?.msg("$response")
    }

}