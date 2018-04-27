package com.gautsch.commons.connectivity;

public interface MqttMessageListener {

	public void onMessage(String topic, String message);

}
