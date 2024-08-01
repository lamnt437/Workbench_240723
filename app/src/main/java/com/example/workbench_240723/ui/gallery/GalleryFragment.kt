package com.example.workbench_240723.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.workbench_240723.databinding.FragmentGalleryBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.view.MotionEvent

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        val TAG = "GalleryFragment"

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val buttonW = findViewById<Button>(R.id.buttonW)
        val buttonW = binding.buttonW
        val buttonA = binding.buttonA
        val buttonD = binding.buttonD
        val buttonS = binding.buttonS
        buttonW.setOnClickListener {
            Log.d("Hello", "........")
            buttonW.isPressed = true
        }

        val buttons = listOf(buttonW, buttonA, buttonS, buttonD)

        buttons.forEach { button ->
            button.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.isPressed = true
                        v.performClick()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        v.isPressed = false
                    }
                }
                true
            }
        }

        GlobalScope.launch {
            delay(5000L)
            Log.d(TAG, "Hello from coroutine")
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}