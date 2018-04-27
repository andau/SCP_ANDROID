package com.gautsch.domain;

import android.hardware.SensorEvent;

import java.text.MessageFormat;

public class ProximitySensorData implements ISensorData {

    public static final String SENSOR_TYPE_NAME = "ProximitySensor";
    int proximityValue;

    public String getSensorName() {
        return SENSOR_TYPE_NAME;
    }

    public void setData(SensorEvent event) {
        proximityValue = (int) event.values[0];
    }

    public void setTextData(String text) {
        throw new UnsupportedOperationException();
    }

    public String getUiOutput() {
        return MessageFormat.format("Sensor data: Proximity: {0}", proximityValue);
    }

    public String[][] getMeasureString() {
        return new String[][]{{Integer.toString(proximityValue)}};
    }

}
