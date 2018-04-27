package com.gautsch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gautsch.domain.ScpConnectionInfo;

public class ScpConnectionInfoFactory {
    public static ScpConnectionInfo createConnectionInfoFromSettings(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String host = preferences.getString("settings_host", "");
        String username = preferences.getString("settings_username", "");
        String password = preferences.getString("settings_password", "");
        String deviceId = preferences.getString("settings_deviceId", "");
        String acclerometerSensorId = preferences.getString("settings_acclerometerSensorId", "");
        String lightSensorId = preferences.getString("settings_lightSensorId", "");
        String proximitySensorId = preferences.getString("settings_proximitySensorId", "");
        String serialnumberSensorId = preferences.getString("settings_serialnumberSensorId", "");

        return new ScpConnectionInfo(host, username, password, deviceId, acclerometerSensorId, lightSensorId, proximitySensorId, serialnumberSensorId);
    }

    public static boolean ValidateSettings(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String host = preferences.getString("settings_host", "");
        String username = preferences.getString("settings_username", "");
        String password = preferences.getString("settings_password", "");

        return (!host.isEmpty() && !username.isEmpty() && !password.isEmpty());
    }
}
