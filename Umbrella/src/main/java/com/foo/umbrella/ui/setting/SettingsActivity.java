package com.foo.umbrella.ui.setting;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.foo.umbrella.R;
import com.foo.umbrella.util.Constants;

public class SettingsActivity extends AppCompatActivity{

    private static final String ZIP_CODE_DEFAULT = "95035";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPreferenceFragment();
        configureActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method will open PreferenceFragment into this activity.
     * @see SettingsFragment
     */
    private void openPreferenceFragment(){
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingsFragment()).commit();
    }

    /**
     * This method will configure ActionBar to have back button and have correct color.
     * The color of the ActionBar will always change, but unfortunately, the status bar will only change for Lollipop and up.
     */
    private void configureActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(this,R.color.settings_actionbar)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.settings_actionbar));
        }
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_preference);
            setPreferenceSummary();
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        /**
         * This method is called when SharePref is changed. It changes the summary text.
         * @param sharedPreferences
         * @param s
         */
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            Preference preference = findPreference(s);
            preference.setSummary(sharedPreferences.getString(s,ZIP_CODE_DEFAULT));
        }

        /**
         * This method sets the summary of the preferences to the sharedPreferences.
         */
        private void setPreferenceSummary(){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            findPreference(Constants.ZIP_KEY).setSummary(sharedPref.getString(Constants.ZIP_KEY,ZIP_CODE_DEFAULT));
            findPreference(Constants.UNIT_KEY).setSummary(sharedPref.getString(Constants.UNIT_KEY,Constants.FAHRENHEIT));
        }
    }
}
