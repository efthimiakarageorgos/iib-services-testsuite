package com.qio.lib.apiHelpers.assetType;

import com.qio.lib.apiHelpers.APIRequestHelper;
import com.qio.lib.connection.ConnectionResponse;

public class MAssetTypeAttributeAPIHelper extends MAssetTypeAPIHelper {
	private final String getAllAssetTypeAttributesEndpoint = "/attributes";
	private final String getSingleAssetTypeAttributeEndpoint = "/attributes/{assetTypeAttributeId}";

	public ConnectionResponse retrieve(String microservice, String environment, String assetTypeId, APIRequestHelper apiRequestHeaders) {
		return super.retrieve(microservice, environment, assetTypeId + getAllAssetTypeAttributesEndpoint, apiRequestHeaders);
	}

	public ConnectionResponse retrieve(String microservice, String environment, String assetTypeId, String assetTypeAttributeId,
			APIRequestHelper apiRequestHeaders) {
		return super.retrieve(microservice, environment, assetTypeId + replaceAssetTypeAttributeIdInSingleAssetTypeAttributeEndpoint(
				assetTypeAttributeId), apiRequestHeaders);
	}

	private String replaceAssetTypeAttributeIdInSingleAssetTypeAttributeEndpoint(String assetTypeAttributeId) {
		String singleAssetTypeAttributeEndpoint = getSingleAssetTypeAttributeEndpoint.replace("{assetTypeAttributeId}", assetTypeAttributeId);
		return singleAssetTypeAttributeEndpoint;
	}
}
