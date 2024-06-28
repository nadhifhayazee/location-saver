package com.nadhifhayazee.shared.tools

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object ImageUriGenerator {

    fun createImageUri(context: Context): Uri {
        val uri = getContentUri()
        val resolver = context.contentResolver
        val contentValues = ContentValues()
        val imageName = "IMAGE_${System.currentTimeMillis()}.jpg"

        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Location Saver")
        return resolver.insert(uri, contentValues) ?: uri

    }

    private fun getContentUri(): Uri {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }


}

suspend fun Uri.getBitmap(context: Context): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(this@getBitmap)
            inputStream?.use {
                BitmapFactory.decodeStream(it)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}