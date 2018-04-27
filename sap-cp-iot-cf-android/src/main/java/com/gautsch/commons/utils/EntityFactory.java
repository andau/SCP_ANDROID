package com.gautsch.commons.utils;

import com.gautsch.commons.model.Capability;
import com.gautsch.commons.model.Device;
import com.gautsch.commons.model.Gateway;
import com.gautsch.commons.model.Property;
import com.gautsch.commons.model.PropertyType;
import com.gautsch.commons.model.Sensor;
import com.gautsch.commons.model.SensorType;
import com.gautsch.commons.model.SensorTypeCapability;
import com.gautsch.commons.model.gateway.Measure;
import com.gautsch.domain.ISensorData;

public class EntityFactory {

	private static final String ANDROID_DEVICE_NAME = "Android phone";

	private static final String ACCELEROMETER_X_PROPERTY_NAME = "AccelerometerX";
	private static final String ACCELEROMETER_Y_PROPERTY_NAME = "AccelerometerY";
	private static final String ACCELEROMETER_Z_PROPERTY_NAME = "AccelerometerZ";
	private static final String ACCELEROMETER_PROPERTY_MEASURE = null;
	private static final String ACCELEROMETER_CAPABILITY_NAME = "Accelerometer_Capability";
	private static final String ACCELEROMETER_CAPABILITY_ALTERNATE_ID = "Accelerometer";

	private static final String LIGHT_PROPERTY_NAME = "Light";
	private static final String LIGHT_PROPERTY_MEASURE = null;
	private static final String LIGHT_CAPABILITY_NAME = "Light_Capability";
	private static final String LIGHT_CAPABILITY_ALTERNATE_ID = "Light";

	private static final String PROXIMITY_PROPERTY_NAME = "Proximity";
	private static final String PROXIMITY_PROPERTY_MEASURE = null;
	private static final String PROXIMITY_CAPABILITY_NAME = "Proximity_Capability";
	private static final String PROXIMITY_CAPABILITY_ALTERNATE_ID = "Proximity";

	private static final String SERIALNUMBER_PROPERTY_NAME = "Serialnumber";
	private static final String SERIALNUMBER_PROPERTY_MEASURE = null;
	private static final String SERIALNUMBER_CAPABILITY_NAME = "Serialnumber_Capability";
	private static final String SERIALNUMBER_CAPABILITY_ALTERNATE_ID = "Serialnumber";


	public static Measure buildMeasure(Sensor sensor, Capability capability, ISensorData sensorData) {
		Measure measure = new Measure();

		measure.setCapabilityAlternateId(capability.getAlternateId());
		measure.setSensorAlternateId(sensor.getAlternateId());
		measure.setMeasures(sensorData.getMeasureString());

		return measure;
	}


	public static Sensor buildSensor(Device device, SensorType sensorType) {
		Sensor sensor = new Sensor();

		sensor.setDeviceId(device.getId());
		sensor.setSensorTypeId(sensorType.getId());
		sensor.setName(sensorType.getName());

		return sensor;
	}

	public static Device buildAndroidDevice(Gateway gateway) {
		Device device = new Device();

		device.setGatewayId(gateway.getId());
		device.setName(ANDROID_DEVICE_NAME);

		return device;
	}

	public static SensorType buildSensorType(String sensorTypeName, Capability capability) {


		SensorType sensorType = new SensorType();
		sensorType.setName(sensorTypeName);

		SensorTypeCapability sensorTypeCapability = new SensorTypeCapability();
		sensorTypeCapability.setId(capability.getId());

		sensorType.setCapabilities(new SensorTypeCapability[] { sensorTypeCapability });

		return sensorType;
	}


	public static Capability buildAccelerometerCapability() {
		Capability capability = new Capability();
		capability.setAlternateId(ACCELEROMETER_CAPABILITY_ALTERNATE_ID); 
		capability.setName(ACCELEROMETER_CAPABILITY_NAME);
		capability.setProperties(new Property[] { 
				buildAccelerometerProperty(ACCELEROMETER_X_PROPERTY_NAME), 
				buildAccelerometerProperty(ACCELEROMETER_Y_PROPERTY_NAME), 
				buildAccelerometerProperty(ACCELEROMETER_Z_PROPERTY_NAME) });
		return capability; 
	}

	public static Capability buildLightCapability() {
		Capability capability = new Capability();
		capability.setAlternateId(LIGHT_CAPABILITY_ALTERNATE_ID);
		capability.setName(LIGHT_CAPABILITY_NAME);
		capability.setProperties(new Property[] {
				buildLightProperty(LIGHT_PROPERTY_NAME) });
		return capability;
	}

	public static Capability buildProximityCapability() {
		Capability capability = new Capability();
		capability.setAlternateId(PROXIMITY_CAPABILITY_ALTERNATE_ID);
		capability.setName(PROXIMITY_CAPABILITY_NAME);
		capability.setProperties(new Property[]{
				buildProximityProperty(PROXIMITY_PROPERTY_NAME)});
		return capability;
	}

	public static Capability buildSerialnumberCapability() {
		Capability capability = new Capability();
		capability.setAlternateId(SERIALNUMBER_CAPABILITY_ALTERNATE_ID);
		capability.setName(SERIALNUMBER_CAPABILITY_NAME);
		capability.setProperties(new Property[]{
				buildSerialnumberProperty(SERIALNUMBER_PROPERTY_NAME)});
		return capability;
	}

	private static Property buildAccelerometerProperty(String acelerometerPropertyName) {
		Property property = new Property();

		property.setName(acelerometerPropertyName);
		property.setDataType(PropertyType.FLOAT);
		property.setUnitOfMeasure(ACCELEROMETER_PROPERTY_MEASURE);

		return property;
	}

	private static Property buildLightProperty(String lightPropertyName) {
		Property property = new Property();

		property.setName(lightPropertyName);
		property.setDataType(PropertyType.INTEGER);
		property.setUnitOfMeasure(LIGHT_PROPERTY_MEASURE);

		return property;
	}

	private static Property buildProximityProperty(String proximityPropertyName) {
		Property property = new Property();

		property.setName(proximityPropertyName);
		property.setDataType(PropertyType.INTEGER);
		property.setUnitOfMeasure(PROXIMITY_PROPERTY_MEASURE);

		return property;
	}

	public static Property buildSerialnumberProperty(String serialnumberPropertyName) {
		Property property = new Property();

		property.setName(serialnumberPropertyName);
		property.setDataType(PropertyType.STRING);
		property.setUnitOfMeasure(SERIALNUMBER_PROPERTY_MEASURE);

		return property;

	}
}
