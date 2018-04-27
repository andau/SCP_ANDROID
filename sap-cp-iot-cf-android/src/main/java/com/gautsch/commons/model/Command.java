package com.gautsch.commons.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Command {

	private String capabilityId;

	private String sensorId;

	@SerializedName("command")
	private Map<String, Object> properties;

	public String getCapabilityId() {
		return capabilityId;
	}

	public void setCapabilityId(String capabilityId) {
		this.capabilityId = capabilityId;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
