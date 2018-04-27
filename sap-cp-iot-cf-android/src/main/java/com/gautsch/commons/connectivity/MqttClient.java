package com.gautsch.commons.connectivity;

import java.io.IOException;

import javax.net.ssl.SSLSocketFactory;

public class MqttClient
extends AbstractClient {


	private String clientId;

	private MqttClient(String clientId) {
		super();

	}

	public MqttClient(String clientId, String user, String password) {
		this(clientId);

	}

	public MqttClient(String clientId, SSLSocketFactory sslSocketFactory) {
		this(clientId);

	}

	@Override
	public void connect(String serverUri)
	throws IOException {
	}


	@Override
	public void disconnect() {
	}

	public <T> void publish(String topic, T payload, Class<T> clazz)
	throws IOException {
	}

	public void subscribe(String topic, final MqttMessageListener listener)
	throws IOException {
    }
}