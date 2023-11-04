package com.example.beta.fragments
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.beta.MainActivity
import com.example.beta.R

class SettingFragment : PreferenceFragmentCompat() {

    private lateinit var navController: NavController


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

    }

    override fun onResume() {
        super.onResume()

        // Ocultar la Action Bar cuando se muestra el fragmento
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()

        // Mostrar la Action Bar cuando se sale del fragmento
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goBackButton = findPreference<Preference>("back_key")
        goBackButton?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            // Realiza la navegación hacia la actividad deseada aquí
            val action = SettingFragmentDirections.actionGlobalNavDrawerPerfil()
            view.findNavController().navigate(action)
            true
        }

        val toggleNightMode = findPreference<SwitchPreferenceCompat>("dark_mode")
        toggleNightMode?.setOnPreferenceChangeListener { _, newValue ->
            val nightMode = if (newValue as Boolean) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        AppCompatDelegate.setDefaultNightMode(nightMode)
        true
    }
    }
}

