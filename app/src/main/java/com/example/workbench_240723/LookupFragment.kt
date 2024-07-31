package com.example.workbench_240723

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.workbench_240723.databinding.FragmentLookupBinding

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
    private lateinit var binding: FragmentLookupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        binding = FragmentLookupBinding.inflate(layoutInflater)

        floorNumberInput = binding.floorNumberInput
        roomNumberInput = binding.roomNumberInput
        getConsumptionButton = binding.getConsumptionButton
        resultTextView = binding.resultTextView

        getConsumptionButton.setOnClickListener {
            val floorNumber = floorNumberInput.text.toString().toIntOrNull()
            val roomNumber = roomNumberInput.text.toString().toIntOrNull()

            if (floorNumber != null && roomNumber != null) {
                getElectricityConsumption(floorNumber, roomNumber)
            } else {
                resultTextView.text = "Please enter valid floor and room numbers"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lookup, container, false)
    }

    private fun getElectricityConsumption(floorNumber: Int, roomNumber: Int) {
        // This is where you'll implement the Google Sheets API call
        // For now, we'll use a placeholder function
        val consumption = fetchConsumptionFromSheet(floorNumber, roomNumber)
        resultTextView.text = "Electricity consumption: $consumption kWh"
    }

    private fun fetchConsumptionFromSheet(floorNumber: Int, roomNumber: Int): Double {
        // Placeholder function - replace with actual API call
        // This is where you'll implement the Google Sheets API logic
        return 0.0
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