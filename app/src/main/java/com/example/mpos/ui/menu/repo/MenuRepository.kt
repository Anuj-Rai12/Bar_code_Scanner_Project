package com.example.mpos.ui.menu.repo

import com.example.mpos.api.menu.MenuApi
import com.example.mpos.data.mnu.MenuType
import com.example.mpos.data.mnu.MnuData
import com.example.mpos.data.mnu.request.MenuItemRequest
import com.example.mpos.data.mnu.response.json.MenuDataResponse
import com.example.mpos.data.mnu.response.json.MenuDataResponseItem
import com.example.mpos.data.mnu.response.json.SubMenu
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
import com.example.mpos.utils.deserializeFromJson
import com.example.mpos.utils.getEmojiByUnicode
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class MenuRepository constructor(retrofit: Retrofit) {
    private val api = buildApi<MenuApi>(retrofit)


    companion object {
        const val err = "Oops Something Went Wrong!!"
        const val nullError = "Cannot load Response \n Please Try Again!!"
        val err_emoji = "${getEmojiByUnicode(0x1F615)} No Data Found!!"
        val loading = "loading.. ${getEmojiByUnicode(0x1F575)}"
    }

    fun getMenuData(request: MenuItemRequest) = flow {
        emit(ApisResponse.Loading("Loading Menu"))
        val data = try {
            val response = api.getMenuResponse(request)
            if (response.isSuccessful) {
                deserializeFromJson<MenuDataResponse>(response.body()?.categoryMenuResult?.value)?.let {
                    ApisResponse.Success(it)
                } ?: ApisResponse.Error(nullError, null)
            } else {
                ApisResponse.Error(err, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)




     fun getSubMenuList(food: MenuDataResponseItem) = flow {
        emit(ApisResponse.Loading(loading))
        val data=try {
            val arr = mutableListOf<MnuData<SubMenu>>()
            food.subMenu.forEachIndexed { index, subMenu ->
                subMenu.description?.let { desc ->
                    arr.add(
                        MnuData(
                            index,
                            desc,
                            MenuType.SubMenu.name,
                            subMenu
                        )
                    )
                }
            }
            ApisResponse.Success(arr)
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
         emit(data)
    }.flowOn(IO)


    /* fun getLoadItemData(subMenu: SubMenu) = flow {
        emit(ApisResponse.Loading(loading))
        val data = try {
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
            ApisResponse.Success(arr)
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)*/

}