package com.gautsch.util;


import android.hardware.Sensor;

import com.gautsch.domain.AccelerometerSensorData;
import com.gautsch.domain.ISensorData;
import com.gautsch.domain.LightSensorData;
import com.gautsch.domain.ProximitySensorData;
import com.gautsch.domain.SerialnumberSensorData;
import com.gautsch.exception.SensorNotImplementedException;

public class SensorDataFactory {
    public static ISensorData create(int sensorType) throws SensorNotImplementedException {
        ISensorData sensorData;

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorData = new AccelerometerSensorData();
                break;
            case Sensor.TYPE_LIGHT:
                sensorData = new LightSensorData();
                break;
            case Sensor.TYPE_PROXIMITY:
                sensorData = new ProximitySensorData();
                break;
            default:
                throw new SensorNotImplementedException();
        }
        return sensorData;
    }

    public static ISensorData create(String sensorType, String text) throws Exception {
        if (sensorType.equals("SERIALNUMBER"))
        {
            return new SerialnumberSensorData(text);
        }
        else
        {
            throw new Exception();
        }
    }
}
