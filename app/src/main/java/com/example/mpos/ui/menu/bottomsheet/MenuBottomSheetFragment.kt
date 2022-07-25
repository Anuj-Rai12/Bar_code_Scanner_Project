package com.example.mpos.ui.menu.bottomsheet


import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.mpos.R
import com.example.mpos.data.barcode.response.json.BarcodeJsonResponse
import com.example.mpos.data.mnu.response.json.ItemList
import com.example.mpos.data.mnu.response.json.MenuDataResponse
import com.example.mpos.databinding.MnuBottomSheetFramgmentLayoutBinding
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.menu.tabs.MnuTabFragment
import com.example.mpos.ui.menu.viewmodel.BottomSheetViewModel
import com.example.mpos.ui.scan.viewmodel.BarCodeViewModel
import com.example.mpos.utils.*
import com.example.mpos.viewpager.ViewPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator


class MenuBottomSheetFragment(private val title: String) : BottomSheetDialogFragment() {


    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private lateinit var binding: MnuBottomSheetFramgmentLayoutBinding
    private val viewModel: BottomSheetViewModel by viewModels()
    private val barCodeViewModel: BarCodeViewModel by viewModels()
    private lateinit var viewPagerAdaptor: ViewPagerAdapter

    var onBottomSheetClickListener: OnBottomSheetClickListener? = null

    private var menuResponse: MenuDataResponse? = null

    companion object {
        const val NAME = "MENU_BOTTOM_SHEET"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.mnu_bottom_sheet_framgment_layout, null)
        binding = MnuBottomSheetFramgmentLayoutBinding.bind(view)
        //setting layout with bottom sheet
        bottomSheet.setContentView(view)
        binding.nameToolbar.text = title
        binding.viewPager.isUserInputEnabled = false
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        //setting Peek at the 16:9 ratio key-line of its parent.
        bottomSheetBehavior?.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
        viewModel.event.observe(this) {
            it.getContentIfNotHandled()?.let { res ->
                showSnackBar(res, R.color.color_red)
                showErrorDialog(msg = res)
            }
        }

        barCodeViewModel.events.observe(this) {
            it.getContentIfNotHandled()?.let { res ->
                if (res is ApisResponse.Error) {
                    showSnackBar("${res.data}", R.color.color_red)
                }
            }
        }

        getMenuItem()
        setTab()
        setCallBack()
        barCodeResult()
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }


        return bottomSheet
    }


    private fun setAdaptor() {
        viewPagerAdaptor = ViewPagerAdapter(childFragmentManager, this.lifecycle)
        binding.viewPager.adapter = viewPagerAdaptor
    }

    private fun setTab() {
        viewModel.mnuTab.observe(this) {
            if (!it.isNullOrEmpty()) {
                setAdaptor()
                it.forEach { title ->
                    setUpFragment(title)
                }
                TabLayoutMediator(binding.tabs, binding.viewPager) { tab, pos ->
                    tab.text = it[pos]
                }.attach()
            }
        }
    }

    private fun setUpFragment(title: String) {
        viewPagerAdaptor.setFragment(MnuTabFragment(title))
    }

    fun getMnuResponse() = menuResponse


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
                    menuResponse = it.data as MenuDataResponse
                    viewModel.setUi(menuResponse)
                }
            }
        }
    }


    private fun showSnackBar(msg: String, color: Int, length: Int = Snackbar.LENGTH_SHORT) {
        binding.root.showSandbar(
            msg,
            length,
            requireActivity().getColorInt(color)
        ) {
            return@showSandbar "OK"
        }
    }


    private fun barCodeResult() {
        barCodeViewModel.barCodeResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    is ApisResponse.Error -> {
                        binding.pbLayoutInclude.root.hide()
                        if (it.data == null) {
                            it.exception?.localizedMessage?.let { err ->
                                showErrorDialog(err)
                            }
                        } else {
                            showErrorDialog("${it.data}")
                        }
                    }
                    is ApisResponse.Loading -> {
                        binding.pbLayoutInclude.root.show()
                        binding.pbLayoutInclude.titleTxt.text="${it.data}"
                    }
                    is ApisResponse.Success -> {
                        binding.pbLayoutInclude.root.hide()
                        (it.data as BarcodeJsonResponse?)?.let { res ->
                            onBottomSheetClickListener?.onItemClicked(res)
                        } ?: showDialogBox(
                            "Failed!!",
                            "Some thing Went Wrong",
                            icon = R.drawable.ic_error
                        ) {}
                    }
                }
            }
        }
    }

    private fun showErrorDialog(msg: String) {
        showDialogBox("Failed", msg, icon = R.drawable.ic_error) {}
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

                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            binding.appBarLayout.show()
                            binding.imageUp.hide()
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

    fun getItem(itemList: ItemList?) {
        if (itemList == null) {
            showSnackBar("Oops Something Went Wong ", R.color.color_red)
            return
        }
        barCodeViewModel.checkForItemItem(itemList.itemCode)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchMenuDetail()
    }

}