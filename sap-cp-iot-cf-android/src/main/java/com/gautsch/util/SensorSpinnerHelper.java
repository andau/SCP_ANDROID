package com.gautsch.util;

import android.hardware.Sensor;

/**
 * Created by GautschA on 20.04.2018.
 */

public class SensorSpinnerHelper {
    public static int convertFromSpinnerValueToSensorId(String spinnerValue)
    {
        switch(spinnerValue)
        {
            case "Light sensor":
                return Sensor.TYPE_LIGHT;
            case "Accelerometer sensor":
                    return Sensor.TYPE_ACCELEROMETER;
            case "Proximity sensor":
                return Sensor.TYPE_PROXIMITY;
            default:
                return Sensor.TYPE_LIGHT;
        }
    }

    public static String convertFromSensorIdToSpinnerValue(int sensorId)
    {
        switch( sensorId)
        {
            case Sensor.TYPE_LIGHT:
                return "Light sensor";
            case Sensor.TYPE_ACCELEROMETER:
                return "Accelerometer sensor";
            case Sensor.TYPE_PROXIMITY:
                return  "Proximity sensor";
            default:
                return "Light sensor";
        }
    }

}
