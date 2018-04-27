package com.gautsch.domain;

import android.hardware.SensorEvent;

public interface ISensorData {
    String getSensorName();
    void setData(SensorEvent event);
    void setTextData(String text);

    String getUiOutput();

    String[][] getMeasureString();
}
