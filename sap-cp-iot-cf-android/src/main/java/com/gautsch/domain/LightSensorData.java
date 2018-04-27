package com.gautsch.domain;

import android.hardware.SensorEvent;

import java.text.MessageFormat;

public class LightSensorData implements ISensorData {

    public static final String SENSOR_TYPE_NAME = "LightSensor";
    int lightValue;

    public String getSensorName() {
        return SENSOR_TYPE_NAME;
    }

    public void setData(SensorEvent event)
    {
        lightValue = (int)event.values[0];
    }

    public void setTextData(String text) {
        throw new UnsupportedOperationException();
    }

    public String getUiOutput()
    {
        return MessageFormat.format("Sensor data: Light: {0}", lightValue);
    }
    public String[][] getMeasureString() {
        return new String[][] { {  Integer.toString(lightValue) } };
    }

}
