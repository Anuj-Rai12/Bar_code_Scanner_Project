package com.example.mpos.data.mnu

data class MnuData(
    val id: Int,
    val title: String,
    val type: String,
)

enum class MenuType {
    SubMenu,
    ItemList
}