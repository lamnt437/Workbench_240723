import com.example.workbench_240723.Constants
import com.example.workbench_240723.RoomService
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import java.io.InputStream

class GoogleSheetsService(private val credentialsStream: InputStream, spreadsheetId: String) {

    private var spreadsheetId: String? = null
    private val sheetsService: Sheets by lazy {
        val credentials = GoogleCredentials.fromStream(credentialsStream)
            .createScoped(listOf(SheetsScopes.SPREADSHEETS))

        Sheets.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            HttpCredentialsAdapter(credentials)
        )
            .setApplicationName("Your App Name")
            .build()
    }

    fun updateCell(range: String, value: String) {
        val values = listOf(listOf(value))
        val body = ValueRange().setValues(values)
        sheetsService.spreadsheets().values()
            .update(this.spreadsheetId, range, body)
            .setValueInputOption("RAW")
            .execute()
    }

    fun getCell(range: String): String {
//        "Sheet1!D1",
        var returnValue = "-1"
         val response = sheetsService.spreadsheets().values()
            .get(this.spreadsheetId, range)
            .execute()
        val values = response.getValues()
        if (values != null && values.isNotEmpty() && values[0].isNotEmpty())
            returnValue = values[0][0].toString()

        return returnValue
    }

    fun getRow(sheetName: String, rowNumber: Int): Any {
        val range = "$sheetName!$rowNumber:$rowNumber"
        val response = sheetsService.spreadsheets().values()
            .get(this.spreadsheetId, range)
            .execute()
        val values = response.getValues()
        if (values != null && values.isNotEmpty()) {
            // Convert the first (and only) row to a List<String>
            return values[0].map { it.toString() }
        } else {
            return "-1"
        }
    }

    // DOING
    fun getRoomRowByMonth(floorNumber: Int, roomNumber: Int, selectedMonth: Int): Any {
        val roomRowIdx = RoomService.getRoomRowIdx(floorNumber, roomNumber)
//        val selectedMonthStr = String.format("%02d", selectedMonth)
//        val sheetName = "$selectedMonthStr-2024"
//        val rowData = this.getRow(sheetName, roomRowIdx)
////        for (i in low..high) {
////            val currentRoom = this.getCell(this.spreadsheetId, "$colRoomId$i")
////            if (currentRoom == "P$roomNumber-T$floorNumber") {
////                row = i
////                break
////            }
////        }
//
//        return rowData
        return 1
    }

    // TODO get the whole row
}