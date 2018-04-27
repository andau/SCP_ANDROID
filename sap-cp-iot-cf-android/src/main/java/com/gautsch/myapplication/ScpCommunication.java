package com.gautsch.myapplication;

import com.gautsch.commons.AbstractScpCommunication;
import com.gautsch.commons.SampleException;
import com.gautsch.commons.api.GatewayCloud;
import com.gautsch.commons.api.GatewayCloudHttp;
import com.gautsch.commons.api.GatewayCloudMqtt;
import com.gautsch.commons.model.Authentication;
import com.gautsch.commons.model.Capability;
import com.gautsch.commons.model.Device;
import com.gautsch.commons.model.Gateway;
import com.gautsch.commons.model.GatewayProtocol;
import com.gautsch.commons.model.Sensor;
import com.gautsch.commons.model.SensorType;
import com.gautsch.commons.model.gateway.Measure;
import com.gautsch.commons.utils.Console;
import com.gautsch.commons.utils.EntityFactory;
import com.gautsch.commons.utils.SecurityUtil;
import com.gautsch.domain.AccelerometerSensorData;
import com.gautsch.domain.ISensorData;
import com.gautsch.domain.LightSensorData;
import com.gautsch.domain.ProximitySensorData;
import com.gautsch.domain.ScpConnectionInfo;
import com.gautsch.domain.SerialnumberSensorData;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

public class ScpCommunication
        extends AbstractScpCommunication {

    private static ScpCommunication instance;

    private GatewayCloud gatewayCloud;
    private ISensorData sensorData;

    private Capability accelerometerCapability;
    private Capability lightCapability;
    private Capability proximityCapability;
    private Capability serialnumberCapability;

    private Sensor accelerometerSensor;
    private Sensor lightSensor;
    private Sensor proximitySensor;
    private Sensor serialnumberSensor;
    private String currentSensor;

    private boolean initialized;

    private ScpCommunication(ScpConnectionInfo scpConnectionInfo)
    {
      this.scpConnectionInfo = scpConnectionInfo;
      init();
    }

    public static ScpCommunication getInstance(ScpConnectionInfo scpConnectionInfo) {
        if (instance == null)
        {
            instance = new ScpCommunication(scpConnectionInfo);
        }

        return instance;
    }


    public ScpConnectionInfo getScpConnectionInfo()
    {
        return scpConnectionInfo;
    }

    @Override
    protected String getDescription() {
        return "Creates an Device for the Android App with an accelerometer sensor";
    }

    @Override
    protected void initialize()
            throws SampleException {
        GatewayProtocol gatewayProtocol = GatewayProtocol
                .fromValue("rest");

        try {
            Console.printSeparator();

            Gateway gateway = coreService.getOnlineCloudGateway(gatewayProtocol);

            Console.printSeparator();

            Device device = getOrAddDevice(scpConnectionInfo.getDeviceId(), gateway);
            scpConnectionInfo.setDeviceId(device.getId());

            Console.printSeparator();

            accelerometerCapability = getOrAddCapability(
                    EntityFactory.buildAccelerometerCapability());

            lightCapability = getOrAddCapability(
                    EntityFactory.buildLightCapability());

            proximityCapability = getOrAddCapability(
                    EntityFactory.buildProximityCapability());

            serialnumberCapability = getOrAddCapability(
                    EntityFactory.buildSerialnumberCapability());

            Console.printSeparator();

            SensorType sensorTypeAccelerometer = getOrAddSensorType(AccelerometerSensorData.SENSOR_TYPE_NAME, accelerometerCapability);
            SensorType sensorTypeLight = getOrAddSensorType(LightSensorData.SENSOR_TYPE_NAME, lightCapability);
            SensorType sensorTypeProximity = getOrAddSensorType(ProximitySensorData.SENSOR_TYPE_NAME, proximityCapability);
            SensorType sensorTypeSerialnumber = getOrAddSensorType(SerialnumberSensorData.SENSOR_TYPE_NAME, serialnumberCapability);

            accelerometerSensor = getOrAddSensor(scpConnectionInfo.getAccelerometerSensorId(), device, sensorTypeAccelerometer);
            lightSensor = getOrAddSensor(scpConnectionInfo.getLightSensorId(), device, sensorTypeLight);
            proximitySensor = getOrAddSensor(scpConnectionInfo.getProximitySensorId(), device, sensorTypeProximity);
            serialnumberSensor = getOrAddSensor(scpConnectionInfo.getSerialnumberSensorId(), device, sensorTypeSerialnumber);

            scpConnectionInfo.setAccelerometerSensorId(accelerometerSensor.getId());
            scpConnectionInfo.setLightSensorId(lightSensor.getId());
            scpConnectionInfo.setProximitySensorId(proximitySensor.getId());
            scpConnectionInfo.setSerialnumberSensorId(serialnumberSensor.getId());


            Console.printSeparator();

            Authentication authentication = coreService.getAuthentication(device);

            SSLSocketFactory sslSocketFactory = SecurityUtil.getSSLSocketFactory(device,
                    authentication);

            switch (gatewayProtocol) {
                case MQTT:
                    gatewayCloud = new GatewayCloudMqtt(device, sslSocketFactory);
                    break;
                case REST:
                default:
                    gatewayCloud = new GatewayCloudHttp(device, sslSocketFactory);
                    break;
            }

            Console.printSeparator();
            initialized = true;

        }
        catch (IOException  | GeneralSecurityException | IllegalStateException e) {
            throw new SampleException(e.getMessage());
        }
    }

    public void setSensorData(ISensorData sensorData)
    {
        this.sensorData = sensorData;
    }


    @Override
    protected void sendSensorData() throws SampleException {
        try {
            switch(sensorData.getSensorName())
            {
                case SerialnumberSensorData.SENSOR_TYPE_NAME:
                    sendMeasures(serialnumberSensor, serialnumberCapability, sensorData);
                    break;
                case AccelerometerSensorData.SENSOR_TYPE_NAME:
                    sendMeasures(accelerometerSensor, accelerometerCapability, sensorData);
                    break;
                default:
                    sendMeasures(lightSensor, lightCapability, sensorData);
            }
        }
        catch (IOException  | IllegalStateException e) {
            throw new SampleException(e.getMessage());
        }
    }


    private void sendMeasures(final Sensor sensor, final Capability capability, final ISensorData sensorData)
            throws IOException {
        try {
            gatewayCloud.connect(scpConnectionInfo.getHost());
        }
        catch (IOException e) {
            throw new IOException("Unable to connect to the Gateway Cloud", e);
        }


        Measure measure = EntityFactory.buildMeasure(sensor, capability, sensorData);
        try {
            gatewayCloud.sendMeasure(measure);
        }
        catch (IOException e) {
            Console.printError(e.getMessage());
        }
        finally {
            Console.printSeparator();
        }

        gatewayCloud.disconnect();

    }

    private void receiveAccelerometerMeasures(final Device device, final Capability capability)
            throws IOException {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(new Runnable() {

            @Override
            public void run() {
                try {
                    coreService.getLatestMeasures(device, capability, 25);
                }
                catch (IOException e) {
                    Console.printError(e.getMessage());
                }
                finally {
                    Console.printSeparator();
                }
            }

        }, 5000, TimeUnit.MILLISECONDS);

        try {
            executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            throw new IOException("Interrupted exception", e);
        }
        finally {
            executor.shutdown();
            coreService.shutdown();
        }
    }

    public boolean isInitialized() {
        return initialized;
    }
}