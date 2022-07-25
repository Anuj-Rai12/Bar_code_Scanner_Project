package com.example.mpos.data.mnu

data class MnuData<T>(
    val id: Int,
    val title: String,
    val type: String,
    val data:T
)

enum class MenuType {
    SubMenu,
    ItemList,
    Food
}