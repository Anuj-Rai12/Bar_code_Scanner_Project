package com.example.mpos.data.reverse

data class ReservationTbl(
    val id: Int,
    val customerName: String,
    val phoneNumber: String,
    val time: String
) {
    companion object {
        private var id = 0
        val listOfReservation = listOf(
            ReservationTbl(
                id = getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
            ReservationTbl(
                getId(),
                "Anuj Rai",
                "+9152856215",
                "11:21 PM"
            ),
        )

        private fun getId(): Int {
            this.id+=1
            return this.id
        }
    }
}