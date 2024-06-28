package com.nadhifhayazee.locationsaver

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.nadhifhayazee.locationsaver.base.LocationSaverApp
import com.nadhifhayazee.locationsaver.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            AppTheme(dynamicColor = false) {
                LocationSaverApp()
            }
        }

    }

}