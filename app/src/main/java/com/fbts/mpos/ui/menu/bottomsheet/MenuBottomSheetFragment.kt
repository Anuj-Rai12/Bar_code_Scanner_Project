package com.fbts.mpos.ui.menu.bottomsheet


import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.fbts.mpos.R
import com.fbts.mpos.data.mnu.MnuData
import com.fbts.mpos.databinding.MnuBottomSheetFramgmentLayoutBinding
import com.fbts.mpos.ui.menu.adaptor.MenuBottomSheetAdaptor
import com.fbts.mpos.utils.getEmojiByUnicode
import com.fbts.mpos.utils.hide
import com.fbts.mpos.utils.msg
import com.fbts.mpos.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MenuBottomSheetFragment(private val title: String) : BottomSheetDialogFragment() {

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private lateinit var binding: MnuBottomSheetFramgmentLayoutBinding
    private lateinit var breakfastAdaptor: MenuBottomSheetAdaptor
    private lateinit var lunchAdaptor: MenuBottomSheetAdaptor
    private lateinit var dinnerAdaptor: MenuBottomSheetAdaptor

    companion object {
        const val NAME = "MENU_BOTTOM_SHEET"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.mnu_bottom_sheet_framgment_layout, null)
        binding = MnuBottomSheetFramgmentLayoutBinding.bind(view)
        binding.nameToolbar.text = title
        binding.breakFastTxt.text = "${getEmojiByUnicode(0x1F968)} Breakfast"
        binding.lunchTxt.text = "${getEmojiByUnicode(0x1F355)} Lunch"
        binding.dinnerTxt.text = "${getEmojiByUnicode( 0x1F371)} Dinner"

        //setting layout with bottom sheet
        bottomSheet.setContentView(view)
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        //setting Peek at the 16:9 ratio key-line of its parent.
        bottomSheetBehavior?.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO



        setCallBack()
        setAdaptorBreakFast()
        setAdaptorLunch()
        setAdaptorDinner()

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        return bottomSheet
    }

    private fun setAdaptorDinner() {
        binding.dinnerRecycleView.apply {
            dinnerAdaptor = MenuBottomSheetAdaptor {
                activity?.msg("$it")
            }
            adapter = dinnerAdaptor
            dinnerAdaptor.submitList(MnuData.getDinner())
        }
    }

    private fun setAdaptorLunch() {
        binding.lunchRecycleView.apply {
            lunchAdaptor = MenuBottomSheetAdaptor {
                activity?.msg("$it")
            }
            adapter = lunchAdaptor
            lunchAdaptor.submitList(MnuData.getLunch())
        }
    }

    private fun setAdaptorBreakFast() {
        binding.breakFastRecycleView.apply {
            breakfastAdaptor = MenuBottomSheetAdaptor {
                activity?.msg("$it")
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
/*

    private fun hideAppBar(view: View) {
        val params = view.layoutParams
        params.height = 0
        view.layoutParams = params
    }

    private fun showView(view: View, size: Int) {
        val params = view.layoutParams
        params.height = size
        view.layoutParams = params
    }

    private fun getActionBarSize(): Int {
        val array =
            context!!.theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        return array.getDimension(0, 0f).toInt()
    }
*/

}