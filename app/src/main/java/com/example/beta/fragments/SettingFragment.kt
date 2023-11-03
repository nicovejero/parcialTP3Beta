import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.example.beta.R

class SettingFragment : PreferenceFragmentCompat() {

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
}
