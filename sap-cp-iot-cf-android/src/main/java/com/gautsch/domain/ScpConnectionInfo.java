package com.gautsch.domain;

public class ScpConnectionInfo {
    private String host;
    private String username;
    private String password;
    private String deviceId;
    private String accelerometerSensorId;
    private String lightSensorId;
    private String proximitySensorId;
    private String serialnumberSensorId;


    public ScpConnectionInfo(String host, String username, String password, String deviceId, String accelerometerSensorId, String lightSensorId, String proximitySensorId, String serialnumberSensorId)
    {
        this.host = host;
        this.username = username;
        this.password = password;
        this.deviceId = deviceId;
        this.accelerometerSensorId =  accelerometerSensorId;
        this.lightSensorId =  lightSensorId;
        this.proximitySensorId = proximitySensorId;
        this.serialnumberSensorId = serialnumberSensorId;
    }

    public String getHost()
    {
        return host;
    }
    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getAccelerometerSensorId()
    {
        return accelerometerSensorId;
    }

    public void setAccelerometerSensorId(String accelerometerSensorId)
    {
        this.accelerometerSensorId = accelerometerSensorId;
    }

    public String getLightSensorId() {
        return lightSensorId;
    }

    public void setLightSensorId(String lightSensorId)
    {
        this.lightSensorId = lightSensorId;
    }

    public String getProximitySensorId() {
        return proximitySensorId;
    }

    public void setProximitySensorId(String proximitySensorId) {
        this.proximitySensorId = proximitySensorId;
    }

    public String getSerialnumberSensorId() {
        return serialnumberSensorId;
    }

    public void setSerialnumberSensorId(String serialnumberSensorId) {
        this.serialnumberSensorId = serialnumberSensorId;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

}

