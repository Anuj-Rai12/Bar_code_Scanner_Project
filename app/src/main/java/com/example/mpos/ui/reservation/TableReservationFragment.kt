package com.example.mpos.ui.reservation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mpos.R
import com.example.mpos.data.reverse.ReservationTbl
import com.example.mpos.databinding.TableReservationLayoutBinding
import com.example.mpos.ui.reservation.adaptor.TblReservationAdaptor
import com.example.mpos.utils.changeStatusBarColor
import com.example.mpos.utils.msg

class TableReservationFragment :Fragment(R.layout.table_reservation_layout) {
    private lateinit var binding:TableReservationLayoutBinding
    private lateinit var tblReservationAdaptor: TblReservationAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor(R.color.semi_white_color_two)
        binding=TableReservationLayoutBinding.bind(view)
        setAdaptor()
        binding.bookTbl.extend()
        binding.bookTbl.setOnHoverListener { _, _ ->
            binding.bookTbl.extend()
            activity?.msg("add new Reservation")
            return@setOnHoverListener true
        }
        onScrollListener()
    }

    private fun onScrollListener() {
        binding.timeReserveList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                if (position>0){
                    binding.bookTbl.shrink()
                }else{
                    binding.bookTbl.extend()
                }

            }
        })
    }

    private fun setAdaptor() {
        binding.timeReserveList.apply {
            tblReservationAdaptor= TblReservationAdaptor {
                activity?.msg("$it")
            }
            adapter=tblReservationAdaptor
        }
        tblReservationAdaptor.submitList(ReservationTbl.listOfReservation)
    }

}