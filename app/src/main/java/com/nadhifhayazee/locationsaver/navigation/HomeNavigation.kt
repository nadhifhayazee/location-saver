package com.nadhifhayazee.locationsaver.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.navigation.NavHostController
import com.nadhifhayazee.locationsaver.screen.Route

class HomeNavigation(val navHostController: NavHostController) {

    fun actionHomeScreenToLocationDetailScreen(id: String) {
        navHostController.navigate(LocationDetailNavigation.createRoute(id))
    }

    fun actionHomeScreenToCreateLocationScreen() {
        navHostController.navigate(LocationCreateNavigation.route)
    }

    fun navigateToEditNote(id: String) {
        navHostController.navigate(Route.EditNote.createRoute(id))
    }

    fun navigateToEditTitle(id: String) {
        navHostController.navigate(Route.EditTitle.createRoute(id))
    }

    fun gotoToDirection(context: Context, latitude: String, longitude: String) {
        val uri = Uri.parse("google.navigation:q=$latitude,$longitude")
        val packageMaps = "com.google.android.apps.maps"

        if (isGoogleMapsInstalled(context)){
            Intent(Intent.ACTION_VIEW).apply {
                data = uri
                setPackage(packageMaps)
                context.startActivity(this)

            }
        }else{
            Toast.makeText(context, "Google Maps tidak terpasang pada device ini.", Toast.LENGTH_SHORT).show()
        }



    }

    private fun isGoogleMapsInstalled(context: Context) : Boolean {
        return try {
            val info: ApplicationInfo =
                context.packageManager.getApplicationInfo("com.google.android.apps.maps", 0)
            true
        }catch (e: PackageManager.NameNotFoundException){
            false
        } catch (e: ActivityNotFoundException) {
            false
        }
    }

    companion object {
        const val route = "home"
    }
}