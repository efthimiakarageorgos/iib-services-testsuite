package com.qio.lib.apiHelpers.assetType;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.connection.ConnectionResponse;

public class MAssetTypeParameterAPIHelper extends MAssetTypeAPIHelper {
	private final String getAllAssetTypeParametersEndpoint = "/parameters";

	public ConnectionResponse retrieve(String microservice, String environment, APIHeaders apiRequestHeaders) {
		return super.retrieve(microservice, environment, getAllAssetTypeParametersEndpoint, apiRequestHeaders);
	}
}
