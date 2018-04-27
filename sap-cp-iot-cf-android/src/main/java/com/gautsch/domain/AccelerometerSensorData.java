package com.gautsch.domain;

import android.hardware.SensorEvent;

public class AccelerometerSensorData implements ISensorData {

    public static final String SENSOR_TYPE_NAME = "AccelerometerSensor";
    private double xValue;
    private double yValue;
    private double zValue;


    public String getSensorName() {
        return SENSOR_TYPE_NAME;
    }

    public void setData(SensorEvent event)
    {
        xValue = event.values[0];
        yValue = event.values[1];
        zValue = event.values[2];
    }

    public void setTextData(String text) {
        throw new UnsupportedOperationException();
    }

    public String getUiOutput()
    {
       return String.format("Sensordata: x: %1$,.2f, y: %2$,.2f, z: %3$,.2f", xValue, yValue, zValue);
    }

    public String[][] getMeasureString() {
        return new String[][] { {  Double.toString(xValue),  Double.toString(yValue),  Double.toString(zValue) } };
    }
}
