package com.nadhifhayazee.shared.exceptions

import android.util.Log

class DataNotFoundException(override val message: String?) : Exception(message) {

    init {
        Log.d(this.javaClass.name, message ?: "data not found")
    }
}