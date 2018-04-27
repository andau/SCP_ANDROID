package com.gautsch.commons.model.gateway;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Command {

	private String sensorAlternateId;

	private String capabilityAlternateId;

	@SerializedName("command")
	private Map<String, Object> properties;

	public String getSensorAlternateId() {
		return sensorAlternateId;
	}

	public void setSensorAlternateId(String sensorAlternateId) {
		this.sensorAlternateId = sensorAlternateId;
	}

	public String getCapabilityAlternateId() {
		return capabilityAlternateId;
	}

	public void setCapabilityAlternateId(String capabilityAlternateId) {
		this.capabilityAlternateId = capabilityAlternateId;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
