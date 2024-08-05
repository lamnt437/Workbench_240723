package com.example.workbench_240723.googlesheets

import GoogleSheetsService

class RoomLocator(private val googleSheetsService: GoogleSheetsService, private val spreadsheetId: String) {

    fun findRoomRow(floorNumber: Int, roomNumber: Int): Int? {
        val roomIdentifier = "P$roomNumber-T$floorNumber"
        var low = 1 // Assuming the data starts from row 1
        var high = 9 // Adjust this to a reasonable maximum number of rooms
        var row = -1
        for (i in low..high) {
            val currentRoom = googleSheetsService.getCell("A$i")
            if (currentRoom == "P$roomNumber-T$floorNumber") {
                row = i
                break
            }
        }

        return row
    }

    fun getRoomConsumption(floorNumber: Int, roomNumber: Int): Double? {
        val rowIndex = findRoomRow(floorNumber, roomNumber)
        return rowIndex?.let {
            // Assuming electricity consumption is in column D
            googleSheetsService.getCell("D$it")?.toDoubleOrNull()
        }
    }
}