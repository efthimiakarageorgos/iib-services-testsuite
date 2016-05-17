package com.qio.lib.apiHelpers;

import com.qio.lib.common.MBaseAPIHelper;
import com.qio.lib.connection.ConnectionResponse;

public class MTenantAPIHelper extends MBaseAPIHelper {
	private final String createOrUpdateTenantEndpoint = "/tenants";
	private final String getOrDeleteSingleTenantEndpointAbstract = "/tenant/{tenantId}";
	private final String getAllTenantsEndpoint = "/tenants";
	
	public ConnectionResponse create(String microservice, String environment, String payload, APIRequestHelper apiRequestHeaders){
		return super.create(microservice, environment, createOrUpdateTenantEndpoint, payload, apiRequestHeaders);
	}
	
	public void delete(String microservice, String environment, String tenantId, APIRequestHelper apiRequestHeaders) {
		super.delete(microservice, environment, replaceTenantIdInSingleTenantEndpoint(tenantId), apiRequestHeaders);
	}

	public String update() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ConnectionResponse retrieve(String microservice, String environment, APIRequestHelper apiRequestHeaders) {
		return super.retrieve(microservice, environment, getAllTenantsEndpoint, apiRequestHeaders);
	}
	
	public ConnectionResponse retrieve(String microservice, String environment, String tenantId, APIRequestHelper apiRequestHeaders) {
		return super.retrieve(microservice, environment, replaceTenantIdInSingleTenantEndpoint(tenantId), apiRequestHeaders);
	}
	
	private String replaceTenantIdInSingleTenantEndpoint(String tenantId) {
		String singleTenantEndpoint = getOrDeleteSingleTenantEndpointAbstract.replace("{tenantId}", tenantId);
		return singleTenantEndpoint;
	}
}
