package com.gautsch.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gautsch.domain.ISensorData;
import com.gautsch.exception.SensorNotImplementedException;
import com.gautsch.util.ScpConnectionInfoFactory;
import com.gautsch.util.SensorDataFactory;

import static android.content.Context.SENSOR_SERVICE;


public class MainFragment extends Fragment implements SensorEventListener {

    Button buttonConnectionToScp;
    Button buttonRun;
    TextView tvSensordata;
    TextView tvNextAction;
    ISensorData sensorData;
    ScpCommunication scpCommunication;
    int activeSensor;
    int tramsmitInterval;
    private boolean connectionSettingsValid;



    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                collectAndSendSensorData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            timerHandler.postDelayed(this, tramsmitInterval);
        }
    };
    Runnable timerRunnableInitializeScp = new Runnable() {
        @Override
        public void run() {
            initializeConnectionToScp();
        }
    };

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        buttonRun = (Button) rootView.findViewById(R.id.button);
        buttonConnectionToScp = (Button) rootView.findViewById(R.id.buttonConnectToScp);
        tvSensordata = (TextView) rootView.findViewById(R.id.tvSensorData);
        tvNextAction = (TextView) rootView.findViewById(R.id.tvNextAction);

        createView();
        return rootView;
    }

    private void createView() {


        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor((Sensor.TYPE_PROXIMITY)), SensorManager.SENSOR_DELAY_NORMAL);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        try {
            String selectedSensorId = preferences.getString("settings_androidSensorId", Integer.toString(Sensor.TYPE_LIGHT));
            activeSensor = Integer.parseInt(selectedSensorId);
        } catch (Exception ex) {
            activeSensor = Sensor.TYPE_LIGHT;
        }
        tramsmitInterval = Integer.parseInt(preferences.getString("settings_transmitInterval", "300"));



        try {
            sensorData = SensorDataFactory.create(activeSensor);
        } catch (SensorNotImplementedException e) {
            try {
                sensorData = SensorDataFactory.create(Sensor.TYPE_LIGHT);
                tvSensordata.setText("Sensor not implemented using default sensor");
            } catch (SensorNotImplementedException e1) {
                tvSensordata.setText("Unexpected problem with default sensor");
            }
        }

        buttonRun.setText(R.string.buttonRun_start);
        buttonRun.setTextColor(Color.WHITE);
        buttonRun.setEnabled(false);
        buttonRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonRun.getText().equals(getResources().getString(R.string.buttonRun_stop))) {
                    timerHandler.removeCallbacks(timerRunnable);
                    showMessage(getResources().getString(R.string.toast_data_transmit_stopped));
                    buttonRun.setText(R.string.buttonRun_continue);
                    buttonRun.setBackgroundColor(Color.YELLOW);
                } else {
                    timerHandler.postDelayed(timerRunnable, 0);
                    showMessage(getResources().getString(R.string.toast_data_transmit_started));
                    buttonRun.setText(R.string.buttonRun_stop);
                    buttonRun.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                }
            }
        });

        connectionSettingsValid = ScpConnectionInfoFactory.ValidateSettings(getContext());
        if (!connectionSettingsValid) {
            buttonConnectionToScp.setText(R.string.buttonConnectionToScp_settings);
        }
        showMessage(null);

    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        if (buttonRun.getText().equals(R.string.buttonRun_stop) ) {
            buttonRun.setText(R.string.buttonRun_continue);
            buttonRun.setBackgroundColor(Color.YELLOW);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        try {
            String selectedSensorId  = preferences.getString("settings_androidSensorId", Integer.toString(Sensor.TYPE_LIGHT));
            activeSensor = Integer.parseInt(selectedSensorId);
        } catch (Exception ex) {
            activeSensor = Sensor.TYPE_LIGHT;
        }
        tramsmitInterval = Integer.parseInt(preferences.getString("settings_transmitInterval", "300"));



        try {
            sensorData = SensorDataFactory.create(activeSensor);
        } catch (SensorNotImplementedException e) {
            try {
                sensorData = SensorDataFactory.create(Sensor.TYPE_LIGHT);
                tvSensordata.setText("Sensor not implemented using default sensor");
            } catch (SensorNotImplementedException e1) {
                tvSensordata.setText("Unexpected problem with default sensor");
            }
        }

        buttonConnectionToScp.setText(R.string.buttonConnectionToScp_initialize);
        buttonConnectionToScp.setBackgroundColor(Color.GRAY);
        buttonConnectionToScp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (buttonConnectionToScp.getText() == getResources().getString(R.string.buttonConnectionToScp_settings)) {
                    openSettings();
                } else if (buttonConnectionToScp.getText() == getResources().getString(R.string.buttonConnectionToScp_disconnect)) {
                    timerHandler.removeCallbacks(timerRunnable);
                    buttonConnectionToScp.setText(R.string.buttonConnectionToScp_initialize);
                    buttonConnectionToScp.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    buttonRun.setText(R.string.buttonRun_start);
                    buttonRun.setBackgroundColor(Color.LTGRAY);
                    buttonRun.setTextColor((Color.WHITE));
                    showMessage(getResources().getString(R.string.toast_disconnected));
                } else {
                    showMessage(getResources().getString(R.string.toast_setting_up_connection));
                    buttonConnectionToScp.setText(R.string.buttonConnectionToScp_initializing);
                    buttonConnectionToScp.setBackgroundColor(Color.YELLOW);
                    timerHandler.postDelayed(timerRunnableInitializeScp, 0);
                }
            }
        });

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensorData != null && event.sensor.getType() == activeSensor) {
            sensorData.setData(event);
            tvSensordata.setText(sensorData.getUiOutput());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void openSettings() {
        startActivity(new Intent(getContext(), SettingsActivity.class));
    }

    private void collectAndSendSensorData() throws Exception {
        sendSensorData(sensorData);
    }

    private void initializeConnectionToScp()
    {

        try {
            scpCommunication = ScpCommunication.getInstance(ScpConnectionInfoFactory.createConnectionInfoFromSettings(getContext()));
            if (!scpCommunication.isInitialized())
            {
                scpCommunication.initialize();
            }

            buttonConnectionToScp.setText(R.string.buttonConnectionToScp_disconnect);
            buttonConnectionToScp.setBackgroundColor(getResources().getColor(R.color.colorGreen));

            buttonRun.setTextColor(Color.BLACK);
            buttonRun.setEnabled(true);

            String deviceId = scpCommunication.getScpConnectionInfo().getDeviceId();
            String accelerometerSensorId = scpCommunication.getScpConnectionInfo().getAccelerometerSensorId();
            String lightSensorId = scpCommunication.getScpConnectionInfo().getLightSensorId();
            String proximitySensorId = scpCommunication.getScpConnectionInfo().getProximitySensorId();
            String serialnumberSensorId = scpCommunication.getScpConnectionInfo().getSerialnumberSensorId();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("settings_deviceId", deviceId);
            editor.putString("settings_accelerometerSensorId", accelerometerSensorId);
            editor.putString("settings_lightSensorId",  lightSensorId);
            editor.putString("settings_proximitySensorId", proximitySensorId);
            editor.putString("settings_serialnumberSensorId", serialnumberSensorId);
            editor.commit();

            showMessage("Connection successful.\nDevice Id: " + deviceId);

        } catch (Exception ex) {
            buttonConnectionToScp.setText(R.string.buttonConnectionToScp_init_failed);
            buttonConnectionToScp.setBackgroundColor(Color.RED);
            showMessage(ex.getMessage());

        }
    }

    private void sendSensorData(ISensorData sensorData) {

        try {
            scpCommunication.setSensorData(sensorData);
            scpCommunication.sendSensorData();
        } catch (Exception ex) {
            showMessage(ex.getMessage());
        }
    }


    private void showMessage()
    {
        showMessage(null);
    }
    private void showMessage(String message) {
        Toast toast;
        String toastMessage;

        if (message != null) {
            toastMessage = message;
        } else {
            if (!(ScpConnectionInfoFactory.ValidateSettings(getContext()))) {
                toastMessage =  getResources().getString(R.string.toast_settings_incomplete);
            } else {
                toastMessage = getResources().getString(R.string.toast_ready_to_connect);
            }
        }
        tvNextAction.setText(toastMessage);
    }


}