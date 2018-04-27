package com.gautsch.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {

    //when closing, recreateApplication tells if we have to recreate application due to changes
    private boolean recreateApplication;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recreateApplication = false;

        setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();


    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //in-application back-button
                finish();
                checkRecreateApplication();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public final void onBackPressed() { //mobile's back-button
        super.onBackPressed();
        finish();
        checkRecreateApplication();
    }

    @Override
    public final void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("settings_host") || key.equals("settings_username") || key.equals("settings_password")){
            recreateApplication = true;
        }
    }

    @Override
    protected final void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected final void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void checkRecreateApplication(){
        if (recreateApplication) {
            recreateApplication = false;
            startActivity(new Intent(this, MainActivity.class)); //restart activity with new settings
        }
    }

}
