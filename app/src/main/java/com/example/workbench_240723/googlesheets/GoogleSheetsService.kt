import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import java.io.InputStream

class GoogleSheetsService(private val credentialsStream: InputStream) {

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

    fun updateCell(spreadsheetId: String, range: String, value: String) {
        val values = listOf(listOf(value))
        val body = ValueRange().setValues(values)
        sheetsService.spreadsheets().values()
            .update(spreadsheetId, range, body)
            .setValueInputOption("RAW")
            .execute()
    }
}