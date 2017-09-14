package com.foo.umbrella.ui.setting;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.foo.umbrella.R;
import com.foo.umbrella.base.UmbrellaApp;

public class SettingsActivity extends AppCompatActivity{

    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.settings_preference); //deprecated
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingsFragment()).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_preference);
            setPrefenceSummary();
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

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            Log.i(TAG, "onSharedPreferenceChanged: ");
            Preference preference = findPreference(s);
            preference.setSummary(sharedPreferences.getString(s,"95035"));
//            ((UmbrellaApp)getActivity().getApplication()).getMainPresenter().updateList(sharedPreferences.getString(s,"95035"));
//
//            if (s.equals("zip")){
//                Log.i(TAG, "onSharedPreferenceChanged: ");
//                Preference zipPreference = findPreference(s);
//                zipPreference.setSummary(sharedPreferences.getString(s,"95035"));
//                ((UmbrellaApp)getActivity().getApplication()).getMainPresenter().updateList(sharedPreferences.getString(s,"95035"));
//            }else if (s.equals("unit")){
//                Log.i(TAG, "onSharedPreferenceChanged: ");
//                Preference zipPreference = findPreference(s);
//                zipPreference.setSummary((sharedPreferences.getString(s,"")));
//                ((UmbrellaApp)getActivity().getApplication()).getMainPresenter().updateList();
//            }
        }

        private void setPrefenceSummary(){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            findPreference("zip").setSummary(sharedPref.getString("zip","95035"));
            findPreference("unit").setSummary(sharedPref.getString("unit","Fahrenheit"));
        }

    }
}
