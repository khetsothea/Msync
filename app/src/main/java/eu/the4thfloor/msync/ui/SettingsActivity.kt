package eu.the4thfloor.msync.ui


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import eu.the4thfloor.msync.R
import eu.the4thfloor.msync.utils.checkSelfPermission
import org.jetbrains.anko.doFromSdk

class SettingsActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        bindPreferenceSummaryToValue(findPreference("calendar_name"))
        bindPreferenceSummaryToValue(findPreference("sync_frequency"))

        doFromSdk(Build.VERSION_CODES.M, { checkCalendarPermissions() })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkCalendarPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_CALENDAR,
                                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CALENDAR,
                                       Manifest.permission.WRITE_CALENDAR), 0)
        }
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.

     * @see .sBindPreferenceSummaryToValueListener
     */
    private fun bindPreferenceSummaryToValue(preference: Preference) {
        // Set the listener to watch for value changes.
        preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener
            .onPreferenceChange(preference,
                                PreferenceManager
                                    .getDefaultSharedPreferences(preference.context)
                                    .getString(preference.key, ""))
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
        val stringValue = value.toString()

        if (preference is ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            val listPreference = preference
            val index = listPreference.findIndexOfValue(stringValue)

            // Set the summary to reflect the new value.
            preference.setSummary(
                if (index >= 0)
                    listPreference.entries[index]
                else
                    null)

        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.summary = stringValue
        }
        true
    }
}
