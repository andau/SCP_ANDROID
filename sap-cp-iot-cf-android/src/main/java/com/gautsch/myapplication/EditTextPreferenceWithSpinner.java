package com.gautsch.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gautsch.util.SensorSpinnerHelper;

import java.lang.reflect.Array;
import java.util.Arrays;

//custom view, which enables to show the actual value of a preference
public class EditTextPreferenceWithSpinner extends EditTextPreference {
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;

    public EditTextPreferenceWithSpinner(Context context) {
        super(context);
        setLayoutResource(R.layout.preference_with_spinner);
    }

    public EditTextPreferenceWithSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.preference_with_spinner);
    }

    public EditTextPreferenceWithSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.preference_with_spinner);
    }

    @Override
    protected final void onBindView(View view) {
        super.onBindView(view);
        spinner = (Spinner) view.findViewById(R.id.pref_spinner);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.sensor_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //simple_spinner_dropdown_item simple_spinner_item
        spinner.setAdapter(adapter);
        String[] spinnerValuesArray = getContext().getResources().getStringArray(R.array.sensor_array);
        String actualSensorId = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("settings_androidSensorId", Integer.toString(Sensor.TYPE_LIGHT));
        int actualValuePosition = Arrays.asList(spinnerValuesArray).indexOf(SensorSpinnerHelper.convertFromSensorIdToSpinnerValue(Integer.parseInt(actualSensorId)));
        spinner.setSelection(actualValuePosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                //TODO remove hardcoded value from general spinner edit text preference
                int sensorId = SensorSpinnerHelper.convertFromSpinnerValueToSensorId(spinner.getSelectedItem().toString());
                editor.putString("settings_androidSensorId", Integer.toString(sensorId));
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }});
    }
}