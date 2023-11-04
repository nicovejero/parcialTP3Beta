package com.example.beta

import android.app.Application
import com.example.beta.core.Config
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ParcialTP3:Application(){
    override fun onCreate() {
        super.onCreate()

        Config.apiKey = resources.getString(R.string.api_key)
        Config.baseUrl = resources.getString(R.string.dogs_api_base_url)
    }
}