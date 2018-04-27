package com.gautsch.domain;

import android.hardware.SensorEvent;

public class SerialnumberSensorData implements ISensorData {

    public static final String SENSOR_TYPE_NAME = "SerialnumberSensor";
    private String serialnumber;

    public SerialnumberSensorData(String text)
    {
        serialnumber = text;
    }

    public String getSensorName() {
        return SENSOR_TYPE_NAME;
    }

    @Override
    public void setData(SensorEvent event) {
        throw new UnsupportedOperationException();
    }

    public void setTextData(String text)
    {
        serialnumber = text;
    }

    public String getUiOutput()
    {
       return String.format("Serialnumber:  %1", serialnumber);
    }

    public String[][] getMeasureString() {
        return new String[][] { {  serialnumber } };
    }
}
