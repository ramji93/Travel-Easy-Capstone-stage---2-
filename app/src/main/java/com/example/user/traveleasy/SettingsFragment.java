package com.example.user.traveleasy;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.simonvt.schematic.annotation.OnCreate;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_general);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sortby_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);


        ListPreference listPreference = (ListPreference) preference;
        int prefIndex = listPreference.findIndexOfValue(PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(),""));
        if (prefIndex >= 0) {
            preference.setSummary(listPreference.getEntries()[prefIndex]);
        }


    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String stringValue = newValue.toString();

        ListPreference listPreference = (ListPreference) preference;
        int prefIndex = listPreference.findIndexOfValue(stringValue);
        if (prefIndex >= 0) {
            preference.setSummary(listPreference.getEntries()[prefIndex]);
        }


       // preference.setSummary(newValue.toString());
        return true;
    }
}
