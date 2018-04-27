package com.gautsch.commons.connectivity;

import com.google.gson.Gson;

import java.io.IOException;

/**
 * An abstraction over connectivity clients.
 */
public abstract class AbstractClient {

	protected Gson jsonParser;

	public AbstractClient() {
		jsonParser = new Gson();
	}

	public abstract void connect(String serverUri)
	throws IOException;

	public abstract void disconnect();

}
