package com.example.mpos.data.mnu

import com.example.mpos.R

data class MnuData(
    val title: String,
    val type: String,
    val img: Int,
) {
    companion object {
        fun getBreakFast(): List<MnuData> {
            val list = ArrayList<MnuData>()
            list.add(MnuData("Small", MnuType.BREAKFAST.name, R.drawable.ic_breakfast))
            list.add(MnuData("Regular", MnuType.BREAKFAST.name, R.drawable.ic_breakfast))
            list.add(MnuData("Large", MnuType.BREAKFAST.name, R.drawable.ic_breakfast))
            return list
        }

        fun getLunch(): List<MnuData> {
            val list = ArrayList<MnuData>()
            list.add(MnuData("Small", MnuType.LUNCH.name, R.drawable.ic_lunch))
            list.add(MnuData("Regular", MnuType.LUNCH.name, R.drawable.ic_lunch))
            list.add(MnuData("Large", MnuType.LUNCH.name, R.drawable.ic_lunch))
            return list
        }


        fun getDinner(): List<MnuData> {
            val list = ArrayList<MnuData>()
            list.add(MnuData("Small", MnuType.DINNER.name, R.drawable.ic_dinner))
            list.add(MnuData("Regular", MnuType.DINNER.name, R.drawable.ic_dinner))
            list.add(MnuData("Large", MnuType.DINNER.name, R.drawable.ic_dinner))
            return list
        }
    }
}

enum class MnuType {
    LUNCH,
    DINNER,
    BREAKFAST
}