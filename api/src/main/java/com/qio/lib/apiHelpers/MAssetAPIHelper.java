package com.qio.lib.apiHelpers;

import com.qio.lib.common.MBaseAPIHelper;
import com.qio.lib.connection.ConnectionResponse;

public class MAssetAPIHelper extends MBaseAPIHelper {
	private final String createOrUpdateAssetEndpoint = "/assets";
	private final String getOrDeleteSingleAssetEndpointAbstract = "/assets/{assetId}";
	private final String getAllAssetsEndpoint = "/assets";
	
	public ConnectionResponse create(String microservice, String environment, String payload, APIHeaders apiRequestHeaders){
		return super.create(microservice, environment, createOrUpdateAssetEndpoint, payload, apiRequestHeaders);
	}
	
	public void delete(String microservice, String environment, APIHeaders apiRequestHeaders, String assetId) {
		super.delete(microservice, environment, replaceAssetIdInSingleAssetEndpoint(assetId), apiRequestHeaders);
	}

	public String update() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ConnectionResponse retrieve(String microservice, String environment, APIHeaders apiRequestHeaders) {
		return super.retrieve(microservice, environment, getAllAssetsEndpoint, apiRequestHeaders);
	}
	
	public ConnectionResponse retrieve(String microservice, String environment, APIHeaders apiRequestHeaders, String assetId) {
		return super.retrieve(microservice, environment, replaceAssetIdInSingleAssetEndpoint(assetId), apiRequestHeaders);
	}
	
	private String replaceAssetIdInSingleAssetEndpoint(String assetId) {
		String singleAssetEndpoint = getOrDeleteSingleAssetEndpointAbstract.replace("{assetId}", assetId);
		return singleAssetEndpoint;
	}
}
