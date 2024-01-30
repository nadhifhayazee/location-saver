package com.nadhifhayazee.shared.exceptions

import android.util.Log

class InsertDataException(override val message: String?) : Exception(message) {

    init {
        Log.d(this.javaClass.name, message ?: "insert data failed")
    }
}