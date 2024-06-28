package com.nadhifhayazee.domain.useCase.locationImage

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class VerifyValidUriUseCase @Inject constructor(
    @ApplicationContext val context: Context
){

    operator fun invoke(uri: Uri): Boolean {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use {
                BitmapFactory.decodeStream(it) != null
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}