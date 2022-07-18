package com.example.mpos.ui.menu.bottomsheet


import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.mpos.R
import com.example.mpos.data.mnu.MnuData
import com.example.mpos.data.mnu.response.json.MenuDataResponse
import com.example.mpos.databinding.MnuBottomSheetFramgmentLayoutBinding
import com.example.mpos.ui.menu.adaptor.MenuBottomSheetAdaptor
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.menu.viewmodel.BottomSheetViewModel
import com.example.mpos.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar


class MenuBottomSheetFragment(private val title: String) : BottomSheetDialogFragment() {

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private lateinit var binding: MnuBottomSheetFramgmentLayoutBinding
    private lateinit var breakfastAdaptor: MenuBottomSheetAdaptor
    private lateinit var lunchAdaptor: MenuBottomSheetAdaptor
    private lateinit var dinnerAdaptor: MenuBottomSheetAdaptor

    private val viewModel: BottomSheetViewModel by viewModels()

    var onBottomSheetClickListener: OnBottomSheetClickListener? = null

    companion object {
        const val NAME = "MENU_BOTTOM_SHEET"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.mnu_bottom_sheet_framgment_layout, null)
        binding = MnuBottomSheetFramgmentLayoutBinding.bind(view)
        //setting layout with bottom sheet
        bottomSheet.setContentView(view)
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        //setting Peek at the 16:9 ratio key-line of its parent.
        bottomSheetBehavior?.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO

        viewModel.event.observe(this) {
            it.getContentIfNotHandled()?.let { res ->
                showSnackBar(res, R.color.color_red)
                showErrorDialog(msg = res)
            }
        }

        getMenuItem()


        deleteResponse()
        return bottomSheet
    }

    private fun getMenuItem() {
        viewModel.mnuItem.observe(this) {
            when (it) {
                is ApisResponse.Error -> {
                    binding.pbLayoutInclude.root.hide()
                    if (it.data == null) {
                        it.exception?.localizedMessage?.let { res ->
                            showErrorDialog(res)
                        }
                    } else {
                        showErrorDialog("${it.data}")
                    }
                }
                is ApisResponse.Loading -> {
                    binding.pbLayoutInclude.root.show()
                    binding.pbLayoutInclude.titleTxt.text = "${it.data}"
                }
                is ApisResponse.Success -> {
                    binding.pbLayoutInclude.root.hide()
                    val mnuDataLink = it.data as MenuDataResponse
                    activity?.msg("$mnuDataLink")
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun showSnackBar(msg: String, color: Int, length: Int = Snackbar.LENGTH_SHORT) {
        binding.root.showSandbar(
            msg,
            length,
            requireActivity().getColorInt(color)
        ) {
            return@showSandbar "OK"
        }
    }


    private fun showErrorDialog(msg: String) {
        showDialogBox("Failed", msg, icon = R.drawable.ic_error) {

        }
    }

    private fun deleteResponse() {
        binding.nameToolbar.text = title
        binding.breakFastTxt.text = "${getEmojiByUnicode(0x1F968)} Breakfast"
        binding.lunchTxt.text = "${getEmojiByUnicode(0x1F355)} Lunch"
        binding.dinnerTxt.text = "${getEmojiByUnicode(0x1F371)} Dinner"


        setCallBack()
        setAdaptorBreakFast()
        setAdaptorLunch()
        setAdaptorDinner()

        recycleViewListener(binding.breakFastRecycleView, binding.breakFastArrowToStart)
        recycleViewListener(binding.lunchRecycleView, binding.lunchArrowToStart)
        recycleViewListener(binding.dinnerRecycleView, binding.dinnerArrowToStart)

        onBackArrowClicked(binding.breakFastArrowToStart, binding.breakFastRecycleView)
        onBackArrowClicked(binding.lunchArrowToStart, binding.lunchRecycleView)
        onBackArrowClicked(binding.dinnerArrowToStart, binding.dinnerRecycleView)

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun onBackArrowClicked(view: View, recyclerView: RecyclerView) {
        view.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
            view.hide()
        }
    }

    private fun recycleViewListener(recyclerView: RecyclerView, view: View) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                if (position > 0) {
                    view.show()
                } else {
                    view.hide()
                }

                when (newState) {
                    SCROLL_STATE_IDLE -> {
                        Log.i("SCROLL", "onScrollStateChanged: SCROLL_STATE_IDLE")
                        //binding.breakFastArrowToStart.hide()
                    }
                    SCROLL_STATE_DRAGGING -> {
                        Log.i("SCROLL", "onScrollStateChanged: SCROLL_STATE_SETTLING")
                        //        view.show()
                    }
                    SCROLL_STATE_SETTLING -> {
                        Log.i("SCROLL", "onScrollStateChanged: SCROLL_STATE_SETTLING")
                        //view.show()
                    }
                }
            }
        })
    }

    private fun setAdaptorDinner() {
        binding.dinnerRecycleView.apply {
            dinnerAdaptor = MenuBottomSheetAdaptor {
                //activity?.msg("$it")
                onBottomSheetClickListener?.onItemClicked(it)
                dismiss()
            }
            adapter = dinnerAdaptor
            dinnerAdaptor.submitList(MnuData.getDinner())
        }
    }

    private fun setAdaptorLunch() {
        binding.lunchRecycleView.apply {
            lunchAdaptor = MenuBottomSheetAdaptor {
                //activity?.msg("$it")
                onBottomSheetClickListener?.onItemClicked(it)
                dismiss()
            }
            adapter = lunchAdaptor
            lunchAdaptor.submitList(MnuData.getLunch())
        }
    }

    private fun setAdaptorBreakFast() {
        binding.breakFastRecycleView.apply {
            breakfastAdaptor = MenuBottomSheetAdaptor {
                //activity?.msg("$it")
                onBottomSheetClickListener?.onItemClicked(it)
                dismiss()
            }
            adapter = breakfastAdaptor
            breakfastAdaptor.submitList(MnuData.getBreakFast())
        }
    }

    private fun setCallBack() {
        bottomSheetBehavior?.addBottomSheetCallback(
            object :
                BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.appBarLayout.hide()
                            binding.imageUp.show()
                            binding.viewSpace.hide()
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            binding.appBarLayout.show()
                            binding.imageUp.hide()
                            binding.viewSpace.show()
                        }
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            dismiss()
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
    }


    override fun onStart() {
        super.onStart()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.imageUp.show()
    }


}