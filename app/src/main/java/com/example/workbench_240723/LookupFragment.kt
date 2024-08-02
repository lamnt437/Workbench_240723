package com.example.workbench_240723

import GoogleSheetsService
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.workbench_240723.databinding.FragmentGalleryBinding
import com.example.workbench_240723.databinding.FragmentLookupBinding
import com.example.workbench_240723.googlesheets.RoomLocator

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

        getConsumptionButton.setOnClickListener {
            Log.d("Lookup Button", "getConsumption")
            val floorNumber = floorNumberInput.text.toString().toIntOrNull()
            val roomNumber = roomNumberInput.text.toString().toIntOrNull()
            Log.d("Lookup Button", "floorNumber $floorNumber")
            Log.d("Lookup Button", "roomNumber $roomNumber")

            if (floorNumber != null && roomNumber != null) {
                Thread {
                    try {
                        getElectricityConsumption(floorNumber, roomNumber)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.start()
            } else {
                resultTextView.text = "Please enter valid floor and room numbers"
            }
        }

//        binding.getConsumptionButton.setOnClickListener {
//            Log.d("MainActivity", "Button clicked")
////            getConsumption()
//        }
        Log.d("Hello", "before")
        return root
    }

    private fun getElectricityConsumption(floorNumber: Int, roomNumber: Int) {
        // This is where you'll implement the Google Sheets API call
        // For now, we'll use a placeholder function
        val consumption = fetchConsumptionFromSheet(floorNumber, roomNumber)
        resultTextView.text = "Electricity consumption: $consumption kWh"
    }

    private fun fetchConsumptionFromSheet(floorNumber: Int, roomNumber: Int): Double {
        // Placeholder function - replace with actual API call
        val credentialsStream = resources.openRawResource(R.raw.gg_credentials)
        sheetsService = GoogleSheetsService(credentialsStream)
        roomLocator = RoomLocator(
            sheetsService,
            "1Y8tjOSrSlB19KNUSckgrZZdfv4bwv63L2QjnkzAU1EY"
        )

        // TODO get D1, not hard code
        val roomRow = roomLocator.findRoomRow(floorNumber, roomNumber)
        if (roomRow == null) {
            resultTextView.text = "Room P$roomNumber - T$floorNumber not found in Google Sheet"
        }


        // This is where you'll implement the Google Sheets API logic
        var cellValue = sheetsService.getCell(
            "1Y8tjOSrSlB19KNUSckgrZZdfv4bwv63L2QjnkzAU1EY",
            "Sheet1!D$roomRow"
        )
        Log.d("GoogleSheet getCell", cellValue)
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