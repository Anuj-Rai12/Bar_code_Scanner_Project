package com.example.mpos.ui.menu.tabs

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.example.mpos.ui.menu.repo.MenuRepository
import com.example.mpos.ui.menu.repo.OnBottomSheetClickListener
import com.example.mpos.ui.menu.viewmodel.BottomSheetViewModel
import com.example.mpos.utils.*
import com.google.android.material.snackbar.Snackbar

class MnuTabFragment constructor(private val title: String) :
    Fragment(R.layout.menu_fragment_layout), OnBottomSheetClickListener {

    private lateinit var binding: MenuFragmentLayoutBinding

    private var sumMenuList: List<MnuData<out Any>>? = null
    private var foodMenuList: List<MnuData<out Any>>? = null

    private var mnuItem: MenuDataResponse? = null
    private val viewModel: BottomSheetViewModel by viewModels()
    private lateinit var adaptor: MenuBottomSheetAdaptor
    private var currentStructureName: String? = null

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
        setRecycleAdaptor()
        Handler(Looper.getMainLooper()).postDelayed({
            if (currentStructureName == null) {
                Log.i("TEST", "onViewCreated: Loading menu!!")
                viewModel.getFoodSubMenuItem(mnuItem, title)
            }
        }, 1000)
        getFoodSubMenuItem()
        binding.root.setOnRefreshListener {
            if (binding.root.isRefreshing) {
                showRecycle()
                if (currentStructureName != null) {
                    Log.i("TEST", "onViewCreated: is FoodMenuList is Null ${foodMenuList == null}")
                    if (foodMenuList != null) {
                        currentStructureName = MenuType.Food.name
                        setRecycleData(foodMenuList!!)
                    } else {
                        viewModel.getFoodSubMenuItem(mnuItem, title)
                    }
                } else {
                    viewModel.getFoodSubMenuItem(mnuItem, title)
                }
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
        viewModel.foodItemMnu.observe(viewLifecycleOwner) { res ->
            when (val it = res.second) {
                is ApisResponse.Error -> {
                    binding.root.isRefreshing = false
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
                    binding.root.isRefreshing = true
                }
                is ApisResponse.Success -> {
                    currentStructureName = res.first
                    Log.i(TAG, "getFoodSubMenuItem: Success $title and $currentStructureName")
                    when (MenuType.valueOf(currentStructureName!!)) {
                        MenuType.SubMenu -> {
                            sumMenuList = it.data as List<MnuData<out Any>>
                        }
                        MenuType.ItemList -> {}
                        MenuType.Food -> {
                            foodMenuList = it.data as List<MnuData<out Any>>
                        }
                    }
                    showRecycle()
                    setRecycleData(it.data as List<MnuData<out Any>>)
                }
            }
        }
    }

    private fun setRecycleData(list: List<MnuData<out Any>>) {
        adaptor.notifyDataSetChanged()
        adaptor.submitList(list)
        binding.itemRecycle.smoothScrollToPosition(0)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: $currentStructureName and $title")
        if (currentStructureName == null) {
            viewModel.getFoodSubMenuItem(mnuItem, title)
        } else {
            when (MenuType.valueOf(currentStructureName!!)) {
                MenuType.SubMenu -> {
                    setRecycleData(sumMenuList!!)
                }
                MenuType.ItemList -> {}
                MenuType.Food -> {
                    setRecycleData(foodMenuList!!)
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
        binding.root.isRefreshing = false
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
        val it = response as MnuData<*>
        when (MenuType.valueOf(it.type)) {
            MenuType.SubMenu -> {
                Log.i("TEST", "OnItemClicked Listener is Clicked")
                val subMenu = it.data as SubMenu
                getArr(subMenu)
                /*handler.postDelayed({
                    val subMenu = it.data as SubMenu
                    viewModel.getFoodItem(subMenu)
                }, 500)*/
                /*if (sumMenuList == null) {
                    val subMenu = it.data as SubMenu
                    viewModel.getFoodItem(subMenu)
                } else {
                    currentStructureName = MenuType.SubMenu.name
                    setRecycleData(sumMenuList!!)
                }*/
            }
            MenuType.ItemList -> {
                (parentFragment as MenuBottomSheetFragment?)?.getItem(it.data as ItemList?)
            }
            MenuType.Food -> {
                activity?.msg("UnWanted Call")
            }
        }
    }

    private fun getArr(subMenu: SubMenu){
        hideRecycle(MenuRepository.loading)
        binding.root.isRefreshing=true
        val res=try {
            val arr = mutableListOf<MnuData<ItemList>>()
            subMenu.itemList?.forEachIndexed { index, itemList ->
                if (!checkFieldValue(itemList.description)) {
                    arr.add(
                        MnuData(
                            index,
                            itemList.description,
                            MenuType.ItemList.name,
                            itemList
                        )
                    )
                }
            }
            arr.sortBy { it.title }
            arr
        }catch (e:Exception){
            null
        }

        if (res.isNullOrEmpty()){
            binding.root.isRefreshing=false
            binding.noDataFound.text=MenuRepository.err_emoji
        }else{
            showRecycle()
            setRecycleData(res)
        }
    }


}