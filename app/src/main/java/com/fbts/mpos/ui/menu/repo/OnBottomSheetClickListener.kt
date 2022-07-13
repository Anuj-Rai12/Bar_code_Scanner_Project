package com.fbts.mpos.ui.menu.repo

interface OnBottomSheetClickListener {
    fun <T> onItemClicked(response: T)
}