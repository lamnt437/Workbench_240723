package com.example.workbench_240723

import java.io.InputStream

class RoomService() {

    companion object {
        fun getRoomRowIdx(floorNumber: Int, roomNumber: Int): Any {
            val roomsPerFloor = Constants.RoomConfiguration.ROOMS_PER_FLOOR
            val roomBeginningRowIdx = Constants.SheetConfiguration.ROOM_BEGINNING_ROW

            var rowIdx = roomBeginningRowIdx
            for (i in 0 until (floorNumber - 1)) {
                rowIdx += roomsPerFloor[i]
            }
            rowIdx += (roomNumber - 1)

            return rowIdx
        }
    }
}