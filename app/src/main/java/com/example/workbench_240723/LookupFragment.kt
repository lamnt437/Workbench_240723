package com.example.workbench_240723

import GoogleSheetsService
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.workbench_240723.databinding.FragmentLookupBinding
import com.example.workbench_240723.googlesheets.RoomLocator
import com.example.workbench_240723.Constants
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LookupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LookupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var floorNumberInput: EditText
    private lateinit var roomNumberInput: EditText
    private lateinit var getConsumptionButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var sheetsService: GoogleSheetsService
    private lateinit var roomLocator: RoomLocator
    private lateinit var progressBar: ProgressBar
    private lateinit var monthSpinner: Spinner
    private var selectedMonth: Int = 0

    private var _binding: FragmentLookupBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Hello", "Lookup")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLookupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        floorNumberInput = binding.floorNumberInput
        roomNumberInput = binding.roomNumberInput
        resultTextView = binding.resultTextView
        getConsumptionButton = binding.getConsumptionButton
        progressBar = binding.progressBar
        monthSpinner = binding.monthSpinner

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.months_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            monthSpinner.adapter = adapter
        }

        // Set up the listener to get the selected month
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Get the selected month as a String
                val selectedMonthStr = parent.getItemAtPosition(position) as String
                selectedMonth = selectedMonthStr.toInt()
                // Show a toast or handle the selected month
//                Toast.makeText(requireContext(), "Selected month: $selectedMonth", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case where no item is selected if necessary
            }
        }

        getConsumptionButton.setOnClickListener {
            Log.d("Lookup Button", "getConsumption")
            val floorNumber = floorNumberInput.text.toString().toIntOrNull()
            val roomNumber = roomNumberInput.text.toString().toIntOrNull()
            progressBar.visibility = View.VISIBLE

            if (floorNumber != null && roomNumber != null) {
                Thread {
                    try {
//                        getElectricityConsumption(floorNumber, roomNumber)
//                        var rowData = sheetsService.getRoomRowByMonth(floorNumber, roomNumber, selectedMonth)
////                        Toast.makeText(requireContext(), rowData.toString())
                        var rowIdx = this.getRoomRowIdx(floorNumber, roomNumber)
                        var rowData = sheetsService.getCell("07-2024!$rowIdx:$rowIdx")
//                        Toast.makeText(requireContext(), rowData.toString(), Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
                        progressBar.visibility = View.GONE
                    }
                }.start()
            } else {
                resultTextView.text = "Cần nhập số tầng và số phòng!"
                progressBar.visibility = View.GONE
            }
        }

//        binding.getConsumptionButton.setOnClickListener {
//            Log.d("MainActivity", "Button clicked")
////            getConsumption()
//        }
        Log.d("Hello", "before")
        return root
    }

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

    private fun getElectricityConsumption(floorNumber: Int, roomNumber: Int) {
        // This is where you'll implement the Google Sheets API call
        // For now, we'll use a placeholder function
        val consumption = fetchConsumptionFromSheet(floorNumber, roomNumber, selectedMonth)
        resultTextView.text = "Chí số điện: $consumption kWh"
    }

    private fun fetchConsumptionFromSheet(floorNumber: Int, roomNumber: Int, selectedMonth: Int): Double {
        // TODO move this service init to a dedicated function
        // Placeholder function - replace with actual API call
        val credentialsStream = resources.openRawResource(R.raw.gg_credentials)
        sheetsService = GoogleSheetsService(credentialsStream, Constants.SheetConfiguration.SHEET_ID)
        // TODO move this room locator logic to google sheet service
        roomLocator = RoomLocator(
            sheetsService,
            "1hmlyiW7EAK6MFo1xYz3Hcfi8t41OCEqq-wNTpbyuvCo"
        )

        // TODO get D1, not hard code
        val roomRow = roomLocator.findRoomRow(floorNumber, roomNumber)
        if (roomRow == null) {
            resultTextView.text = "Không tìm thấy P$roomNumber - T$floorNumber"
        }

        // TODO format month into 2 digit form
        val selectedMonthStr = String.format("%02d", selectedMonth)

        // TODO
        val elNewIdxCol = Constants.SheetConfiguration.EL_NEW_IDX_COL

        // This is where you'll implement the Google Sheets API logic
        var cellValue = sheetsService.getCell(
            "$selectedMonthStr-2024!$elNewIdxCol$roomRow"
        )

        // TODO testing getting full row
        var rowValue = sheetsService.getRoomRowByMonth(floorNumber, roomNumber, selectedMonth)

        val gson = Gson()
        Log.d("GoogleSheet getCell", gson.toJson(rowValue))
        return cellValue.toDouble()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LookupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LookupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}