package com.qio.lib.apiHelpers;

import com.qio.lib.common.MBaseAPIHelper;
import com.qio.lib.connection.ConnectionResponse;

public class MAssetTypeAPIHelper extends MBaseAPIHelper {
	private final String createOrUpdateAssetTypeEndpoint = "/assettypes";
	private final String getOrDeleteSingleAssetTypeEndpointAbstract = "/assettypes/{assetTypeId}";
	private final String getAllAssetTypesEndpoint = "/assettypes";
	
	public ConnectionResponse create(String microservice, String environment, String payload, APIHeaders apiRequestHeaders){
		return super.create(microservice, environment, createOrUpdateAssetTypeEndpoint, payload, apiRequestHeaders);
	}
	
	public void delete(String microservice, String environment, String assetTypeId, APIHeaders apiRequestHeaders) {
		super.delete(microservice, environment, replaceAssetTypeIdInSingleAssetTypeEndpoint(assetTypeId), apiRequestHeaders);
	}

	public ConnectionResponse update(String microservice, String environment, String payload,
			String assetTypeId, APIHeaders apiRequestHeaders) {
		return super.update(microservice, environment, replaceAssetTypeIdInSingleAssetTypeEndpoint(assetTypeId),
				payload, apiRequestHeaders);
	}
	
	public ConnectionResponse retrieve(String microservice, String environment, APIHeaders apiRequestHeaders) {
		return super.retrieve(microservice, environment, getAllAssetTypesEndpoint, apiRequestHeaders);
	}
	
	public ConnectionResponse retrieve(String microservice, String environment, String assetTypeId, APIHeaders apiRequestHeaders) {
		return super.retrieve(microservice, environment, replaceAssetTypeIdInSingleAssetTypeEndpoint(assetTypeId), apiRequestHeaders);
	}
	
	private String replaceAssetTypeIdInSingleAssetTypeEndpoint(String assetTypeId) {
		String singleAssetTypeEndpoint = getOrDeleteSingleAssetTypeEndpointAbstract.replace("{assetTypeId}", assetTypeId);
		return singleAssetTypeEndpoint;
	}
}
