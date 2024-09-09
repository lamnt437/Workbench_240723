package com.example.workbench_240723.data

import android.util.Log

class UnitPriceRepo {
    companion object {
        public fun setData(type: String, value: Int) {
            Log.d("UnitPriceRepo", "Unit price type $type with value $value")
        }
    }
}