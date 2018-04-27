package com.gautsch.commons;

import com.gautsch.commons.api.CoreService;
import com.gautsch.commons.model.Capability;
import com.gautsch.commons.model.Device;
import com.gautsch.commons.model.Gateway;
import com.gautsch.commons.model.Sensor;
import com.gautsch.commons.model.SensorType;
import com.gautsch.commons.utils.Console;
import com.gautsch.commons.utils.EntityFactory;
import com.gautsch.domain.ScpConnectionInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScpCommunication
        extends AbstractSample {

    protected CoreService coreService;
    protected ScpConnectionInfo scpConnectionInfo;

    public AbstractScpCommunication() {
        super();

    }

    protected void init()
    {
        coreService = new CoreService(scpConnectionInfo.getHost(), scpConnectionInfo.getUsername(), scpConnectionInfo.getPassword());
    }

    protected Device getOrAddDevice(String deviceId, Gateway gateway)
            throws IOException {

        Device device;
        try {
           device = coreService.getOnlineDevice(deviceId, gateway);
        }
        catch (IOException | IllegalStateException e) {
            Console.printWarning(e.getMessage());

            Console.printSeparator();

            Device deviceTemplate = EntityFactory.buildAndroidDevice(gateway);
            device = coreService.addDevice(deviceTemplate);

            Console.printNewLine();
            Console.printProperty(DEVICE_ID, device.getId());
        }

        return device;
    }

    protected Sensor getOrAddSensor(String sensorId, Device device, SensorType sensorType)
            throws IOException {
        Sensor sensor = null;
        Sensor[] sensors = device.getSensors();
        if (sensors != null) {
            for (int i = 0; i < sensors.length; i++) {
                Sensor nextSensor = sensors[i];
                if (nextSensor.getId().equals(sensorId)) {
                    sensor = nextSensor;
                    break;
                }
            }
        }
        if (sensor != null) {
            if (sensor.getSensorTypeId().equals(sensorType.getId())) {
                return sensor;
            }
            else {
                Console.printWarning(
                        String.format("A Sensor '%1$s' has no reference to Sensor Type '%2$s'",
                                sensorId, sensorType.getId()));
            }
        }
        else {
            Console.printWarning(String.format("No Sensor '%1$s' is attached to the Device '%2$s'",
                    sensorId, device.getId()));
        }

        Console.printSeparator();

        Sensor sensorTemplate = EntityFactory.buildSensor(device, sensorType);
        sensor = coreService.addSensor(sensorTemplate);

        Console.printNewLine();
        Console.printProperty(SENSOR_ID, sensor.getId());

        return sensor;
    }

    protected SensorType getOrAddSensorType(String sensorTypeName, Capability capability)
            throws IOException {
        SensorType sensorTypeTemplate = EntityFactory.buildSensorType(sensorTypeName, capability);

        SensorType[] existingSensorTypes = coreService.getSensorTypes();

        List<SensorType> filteredSensorTypes = new ArrayList<>();
        for(int i = 0; i < existingSensorTypes.length; i++)
        {
            if (existingSensorTypes[i].equals(sensorTypeTemplate))
            {
                filteredSensorTypes.add(existingSensorTypes[i]);
            }
        }
        //Lambda expressions can be used only with Java8
        //List<SensorType> filteredSensorTypes = Arrays.stream(existingSensorTypes)
        //        .filter(st -> st.equals(sensorTypeTemplate)).distinct().collect(Collectors.toList());

        if (filteredSensorTypes.size() == 1) {
            return filteredSensorTypes.get(0);
        }

        Console.printWarning(
                String.format("No '%1$s' Sensor Type found", sensorTypeTemplate.getName()));

        Console.printSeparator();

        SensorType sensorType = coreService.addSensorType(sensorTypeTemplate);

        Console.printNewLine();
        Console.printProperty(SENSOR_TYPE_ID, sensorType.getId());

        return sensorType;
    }

    protected Capability getOrAddCapability(Capability capabilityTemplate)
            throws IOException {

        Capability[] existingCapabilities = coreService.getCapabilities();

        List<Capability> filteredCapabilities = new ArrayList<>();
        for(int i = 0; i < existingCapabilities.length; i++)
        {
            if (existingCapabilities[i].equals(capabilityTemplate))
            {
                filteredCapabilities.add(existingCapabilities[i]);
            }
        }

        //Lambda expressions can be used only with Java 8
        //List<Capability> filteredCapabilities = Arrays.stream(existingCapabilities).distinct()
        //       .filter(c -> c.equals(capabilityTemplate)).collect(Collectors.toList());

        if (filteredCapabilities.size() == 1) {
           return filteredCapabilities.get(0);
        }

        Console.printWarning(
                String.format("No '%1$s' Capability found", capabilityTemplate.getName()));

        Console.printSeparator();

        Capability capability = coreService.addCapability(capabilityTemplate);

        Console.printNewLine();
        Console.printProperty(CAPABILITY_ID, capability.getId());

        return capability;
    }

}