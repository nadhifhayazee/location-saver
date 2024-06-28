package com.nadhifhayazee.locationsaver.tools

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast

class ImplicitIntent {

    companion object {
        fun shareLocation(context: Context, name: String?, lat: Double?, lng: Double?) {
            val header = "Berbagi lokasi $name \n"
            val message = "$header https://www.google.com/maps/dir/?api=1&destination=$lat,$lng"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    header
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    message
                )
            }
            context.startActivity(
                Intent.createChooser(
                    intent,
                    message
                )
            )
        }

        fun toMapsDirection(context: Context, latitude: String, longitude: String) {
            val uri = Uri.parse("google.navigation:q=$latitude,$longitude")
            val packageMaps = "com.google.android.apps.maps"

            if (isGoogleMapsInstalled(context)) {
                Intent(Intent.ACTION_VIEW).apply {
                    data = uri
                    setPackage(packageMaps)
                    context.startActivity(this)

                }
            } else {
                Toast.makeText(
                    context,
                    "Google Maps tidak terpasang pada device ini.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        private fun isGoogleMapsInstalled(context: Context): Boolean {
            return try {
                val info: ApplicationInfo =
                    context.packageManager.getApplicationInfo("com.google.android.apps.maps", 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            } catch (e: ActivityNotFoundException) {
                false
            }
        }
    }
}