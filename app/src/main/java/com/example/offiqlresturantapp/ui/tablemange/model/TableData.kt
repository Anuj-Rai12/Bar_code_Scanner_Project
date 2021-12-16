package com.example.offiqlresturantapp.ui.tablemange.model

// Sample Data Class

data class TableData(
    val isOpen: Boolean = true,
    val isBooked: Boolean = false,
    val isOccupied: Boolean = false,

    val totalPeople: Int = 0,
    val totalTime: Int = 0,

    val msg: String?,

    val tbl: String
)
