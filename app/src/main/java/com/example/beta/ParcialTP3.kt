package com.example.beta

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.beta.core.Config
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ParcialTP3:Application(){
    override fun onCreate() {
        super.onCreate()

        Config.apiKey = resources.getString(R.string.api_key)
        Config.baseUrl = resources.getString(R.string.dogs_api_base_url)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isNightModeEnabled = sharedPreferences.getBoolean("night_mode", false)
        val nightMode = if (isNightModeEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}