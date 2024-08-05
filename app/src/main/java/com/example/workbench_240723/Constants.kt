package com.example.workbench_240723

class Constants {
    object SheetConfiguration {
        const val SHEET_ID = "1hmlyiW7EAK6MFo1xYz3Hcfi8t41OCEqq-wNTpbyuvCo"
        const val ROOM_ID_COL = 'A'
        const val ROOM_BEGINNING_ROW = 3
        const val EL_NEW_IDX_COL = 'B'
        const val EL_LAST_IDX_COL = 'C'
        const val EL_CONSUMPTION_COL = 'D'
        const val EL_UNIT_PRICE_COL = 'E'
        const val EL_COST_COL = 'F'
    }
    object RoomConfiguration {
        val ROOMS_PER_FLOOR = intArrayOf(2, 4, 4, 4, 4, 3)
    }
}