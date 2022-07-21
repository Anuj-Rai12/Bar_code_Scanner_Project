package com.example.mpos.ui.menu.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mpos.R
import com.example.mpos.data.mnu.MenuType
import com.example.mpos.data.mnu.MnuData
import com.example.mpos.data.mnu.response.json.ItemList
import com.example.mpos.data.mnu.response.json.MenuDataResponse
import com.example.mpos.data.mnu.response.json.SubMenu
import com.example.mpos.databinding.MenuFragmentLayoutBinding
import com.example.mpos.ui.menu.adaptor.MenuBottomSheetAdaptor
import com.example.mpos.ui.menu.bottomsheet.MenuBottomSheetFragment
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.menu.viewmodel.BottomSheetViewModel
import com.example.mpos.utils.*
import com.google.android.material.snackbar.Snackbar

class MnuTabFragment constructor(private val title: String) :
    Fragment(R.layout.menu_fragment_layout), OnBottomSheetClickListener {

    private lateinit var binding: MenuFragmentLayoutBinding

    //   private var enumTitle = MenuType.SubMenu.name
    private var mnuItem: MenuDataResponse? = null
    private val viewModel: BottomSheetViewModel by viewModels()
    private lateinit var adaptor: MenuBottomSheetAdaptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MenuFragmentLayoutBinding.bind(view)
        mnuItem = (parentFragment as MenuBottomSheetFragment?)?.getMnuResponse()
        binding.noDataFound.text = "${getEmojiByUnicode(0x1F615)} No Data Found!!"
        viewModel.event.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { res ->
                showSnackBar(res, R.color.color_red)
            }
        }
        viewModel.getFoodSubMenuItem(mnuItem, title)

        setRecycleAdaptor()
        getFoodSubMenuItem()

        binding.root.setOnRefreshListener {
            if (binding.root.isRefreshing) {
                viewModel.getFoodSubMenuItem(mnuItem, title)
                binding.root.isRefreshing = false
            }
        }
    }

    private fun setRecycleAdaptor() {
        binding.itemRecycle.apply {
            setHasFixedSize(true)
            this@MnuTabFragment.adaptor = MenuBottomSheetAdaptor()
            this@MnuTabFragment.adaptor.listener = this@MnuTabFragment
            adapter = this@MnuTabFragment.adaptor
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getFoodSubMenuItem() {
        viewModel.foodItemMnu.observe(viewLifecycleOwner) {
            when (it) {
                is ApisResponse.Error -> {
                    if (it.data != null) {
                        hideRecycle("${it.data}")
                    } else {
                        hideRecycle("")
                        it.exception?.localizedMessage?.let { err ->
                            showDialogBox("Failed", err, icon = R.drawable.ic_error) {}
                        }
                    }
                }
                is ApisResponse.Loading -> {
                    hideRecycle("${it.data}")
                }
                is ApisResponse.Success -> {
                    showRecycle()
                    adaptor.notifyDataSetChanged()
                    adaptor.submitList(it.data as List<MnuData<out Any>>)
                }
            }
        }
    }

    private fun hideRecycle(txt: String) {
        binding.itemRecycle.hide()
        binding.noDataFound.show()
        binding.noDataFound.text = txt
    }

    private fun showRecycle() {
        binding.itemRecycle.show()
        binding.noDataFound.hide()
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

    override fun <T> onItemClicked(response: T) {
        val it= response as MnuData<*>
        when (MenuType.valueOf(it.type)) {
            MenuType.SubMenu -> {
                val subMenu = it.data as SubMenu
                viewModel.getFoodItem(subMenu)
            }
            MenuType.ItemList -> {
                (parentFragment as MenuBottomSheetFragment?)?.getItem(it.data as ItemList?)
            }
        }
    }


}