package com.gautsch.commons;

import com.gautsch.commons.connectivity.ProxySelector;
import com.gautsch.commons.model.GatewayProtocol;
import com.gautsch.commons.utils.Console;
import com.gautsch.commons.utils.Constants;
import com.gautsch.commons.utils.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * An abstraction over all sample applications.
 */
public abstract class AbstractSample {

	public static final String IOT_HOST = "iot.host";
	public static final String IOT_USER = "iot.user";
	public static final String IOT_PASSWORD = "iot.password";
	public static final String DEVICE_ID = "device.id";
	public static final String SENSOR_ID = "sensor.id";
	public static final String SENSOR_TYPE_ID = "sensor.type.id";
	public static final String CAPABILITY_ID = "capability.id";
	public static final String GATEWAY_PROTOCOL_ID = "gateway.protocol.id";
	public static final String PROXY_PORT = "proxy.port";
	public static final String PROXY_HOST = "proxy.host";
	private static final String PRODUCT_TITLE = "SAP Internet of Things for the Cloud Foundry Environment";
	private static final String CONFIGURATIONS_FILE_NAME = "sample.properties";
	protected Properties properties;

	public AbstractSample() {
		Console.printNewLine();
		Console.printText(PRODUCT_TITLE);
		Console.printText(getDescription());
		Console.printNewLine();

	}

	/**
	 * Gets a description of the sample application.
	 */
	protected abstract String getDescription();

	/**
	 * Runs the logic of the sample application.
	 */
	protected abstract void initialize()
	throws SampleException;

	protected abstract void sendSensorData()
			throws SampleException;

	/**
	 * Prompts the user for missing configuration properties.
	 */
	protected void promptProperties() {
		Console console = Console.getInstance();

		String host = properties.getProperty(IOT_HOST);
		//host = console.awaitNextLine(host, "Hostname (e.g. 'test.cp.iot.sap'): ");
		host = "hella-trial.eu10.cp.iot.sap"; 
		properties.setProperty(IOT_HOST, host);

		String user = properties.getProperty(IOT_USER);
		//user = console.awaitNextLine(user, "Username (e.g. 'root'): ");
		user = "root"; 
		properties.setProperty(IOT_USER, user);

		String gatewayType = properties.getProperty(GATEWAY_PROTOCOL_ID);
		//gatewayType = console.awaitNextLine(gatewayType,
		//	"Gateway Protocol ID ('rest' or 'mqtt'): ");
		gatewayType = "rest"; 
		properties.setProperty(GATEWAY_PROTOCOL_ID,
			GatewayProtocol.fromValue(gatewayType).getValue());

		String deviceId = properties.getProperty(DEVICE_ID);
		//deviceId = console.awaitNextLine(deviceId, "Device ID (e.g. '100'): ");
		deviceId = "999"; 
		//TODO Set random number for Device Id  
		properties.setProperty(DEVICE_ID, deviceId);

		String sensorId = properties.getProperty(SENSOR_ID);
		//sensorId = console.awaitNextLine(sensorId, "Sensor ID (e.g. '100'): ");
		sensorId = "999"; 
		properties.setProperty(SENSOR_ID, sensorId);

		String proxyHost = properties.getProperty(PROXY_HOST);
		if (proxyHost == null) {
			//proxyHost = console.nextLine("Proxy Host (e.g. 'proxy' or leave empty): ");
			proxyHost = ""; 
			properties.setProperty(PROXY_HOST, proxyHost);
		}

		String proxyPort = properties.getProperty(PROXY_PORT);
		if (proxyPort == null) {
			//proxyPort = console.nextLine("Proxy Port (e.g. '8080' or leave empty): ");
			proxyPort = ""; 
			properties.setProperty(PROXY_PORT, proxyPort);
		}

		String password = properties.getProperty(IOT_PASSWORD);
		//password = console.nextPassword("Password for your user: ");
		password = "Cx1IiVeVUh4JGTD"; 
		properties.setProperty(IOT_PASSWORD, password);

		console.close();
	};

	/**
	 * Reads the configuration properties from the file located in the same directory to JAR
	 * archive. Sticks to the empty properties collection if the configuration file does not exist.
	 */
	private void init() {
		File jar = new File(
			AbstractSample.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String path = jar.getParentFile().getAbsolutePath()
			.concat(System.getProperty("file.separator")).concat(CONFIGURATIONS_FILE_NAME);
		try {
			path = URLDecoder.decode(path, Constants.DEFAULT_ENCODING.name());
		}
		catch (UnsupportedEncodingException e) {
			Console.printWarning("Unable to decode config file path.");
		}
		File config = new File(path);

		properties = new Properties();

		try {
			if (config.exists()) {
				properties = FileUtil.readProperties(new FileInputStream(config));
			}
		}
		catch (IOException e) {
			// do nothing
		}
		finally {
			promptProperties();
			printProperties();
		}

		setProxy();
	}

	/**
	 * Prints out the resulting configuration properties to the console. Skips user password and
	 * properties having empty values.
	 */
	private void printProperties() {
		Console.printNewLine();
		Console.printText("Properties:");
		for (Object key : properties.keySet()) {
			if (IOT_PASSWORD.equals(key) || properties.get(key).toString().trim().isEmpty()) {
				continue;
			}
			Console.printProperty(key, properties.get(key));
		}
		Console.printNewLine();
	}

	private void setProxy() {
		String proxyHost = properties.getProperty(PROXY_HOST);
		String proxyPort = properties.getProperty(PROXY_PORT);

		ProxySelector.setProxy(proxyHost, proxyPort);
	}

}