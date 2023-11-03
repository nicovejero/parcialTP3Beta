package com.example.beta.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.beta.R

class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}