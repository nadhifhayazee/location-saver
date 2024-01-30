package com.nadhifhayazee.locationsaver

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.nadhifhayazee.locationsaver.base.LocationSaverApp
import com.nadhifhayazee.locationsaver.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding?.root)

        setContent {

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {

                LocationSaverApp()


            }

        }

    }

}