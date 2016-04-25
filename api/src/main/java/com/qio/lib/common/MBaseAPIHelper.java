package com.qio.lib.common;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.connection.ConnectionManager;
import com.qio.lib.connection.ConnectionResponse;

public class MBaseAPIHelper {
	ConnectionManager conManager = null;

	public ConnectionResponse create(String microservice, String environment, String endpoint, String payload, APIHeaders apiRequestHeaders){
		initConManager();
		return conManager.post(getURI(microservice, environment, endpoint), payload, apiRequestHeaders);
	}

	public void delete(String microservice, String environment, String endpoint, APIHeaders apiRequestHeaders){
		initConManager();
		conManager.delete(getURI(microservice, environment, endpoint), apiRequestHeaders);
	}
	
	public ConnectionResponse update(String microservice, String environment, String endpoint, String payload,
			APIHeaders apiRequestHeaders) {
		initConManager();
		return conManager.put(getURI(microservice, environment, endpoint), payload, apiRequestHeaders);
	}
	
	public ConnectionResponse retrieve(String microservice, String environment, String endpoint, APIHeaders apiRequestHeaders){
		initConManager();
		return conManager.get(getURI(microservice, environment, endpoint), apiRequestHeaders);
	}
	
	private void initConManager(){
		conManager = conManager == null ? ConnectionManager.getInstance() : conManager;
	}
	
	private String getURI(String microservice, String environment, String endpoint) {
		String URI = "http://" + microservice + environment + endpoint;
		return URI;
	}
}