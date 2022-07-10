package com.fbts.mpos.data.mnu

import com.fbts.mpos.R

data class MnuData(
    val title: String,
    val type: String,
    val img: Int,
) {
    companion object {
        fun getBreakFast(): List<MnuData> {
            val list = ArrayList<MnuData>()
            list.add(MnuData("Menu", MnuType.BREAKFAST.name, R.drawable.ic_breakfast))
            return list
        }

        fun getLunch(): List<MnuData> {
            val list = ArrayList<MnuData>()
            list.add(MnuData("Menu", MnuType.LUNCH.name, R.drawable.ic_lunch))
            return list
        }


        fun getDinner(): List<MnuData> {
            val list = ArrayList<MnuData>()
            list.add(MnuData("Menu", MnuType.DINNER.name, R.drawable.ic_dinner))
            return list
        }
    }
}

enum class MnuType {
    LUNCH,
    DINNER,
    BREAKFAST
}