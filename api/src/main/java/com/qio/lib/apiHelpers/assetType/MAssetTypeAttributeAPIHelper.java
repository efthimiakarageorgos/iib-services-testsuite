package com.qio.lib.apiHelpers.assetType;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.connection.ConnectionResponse;

public class MAssetTypeAttributeAPIHelper extends MAssetTypeAPIHelper {
	private final String getAllAssetTypeAttributesEndpoint = "/attributes";

	public ConnectionResponse retrieve(String microservice, String environment, APIHeaders apiRequestHeaders) {
		return super.retrieve(microservice, environment, getAllAssetTypeAttributesEndpoint, apiRequestHeaders);
	}
}
