package com.qio.lib.apiHelpers.analytics;

import com.qio.lib.apiHelpers.APIRequestHelper;
import com.qio.lib.common.MBaseAPIHelper;
import com.qio.lib.connection.ConnectionResponse;

//http://analytics-mvp2.qiotec-internal.com/output/56daf4bdce54844013eccd37/5720c00fb38fcd4e5f2cff65?start=2030-01-01T00:20:00.000Z&end=2030-01-01T01:10:00.000Z&outputList=5720babbb38fcd4e5f2a169f

public class MAnalyticAssetMapAPIHelper extends MBaseAPIHelper {
	private final String createOrUpdateUserGroupEndpoint = "/analyticassetmap";
	private final String getOrDeleteSingleUserGroupEndpointAbstract = "/analyticassetmap/{analyticAssetMapId}";
	private final String getAllUserGroupsEndpoint = "/analyticassetmap";

	public String getGetOrDeleteSingleUserGroupEndpointAbstract() {
		return getOrDeleteSingleUserGroupEndpointAbstract;
	}

	public ConnectionResponse create(String microservice, String environment, String payload, APIRequestHelper apiRequestHeaders) {
		return super.create(microservice, environment, createOrUpdateUserGroupEndpoint, payload, apiRequestHeaders);
	}

	public void delete(String microservice, String environment, String analyticAssetMapId, APIRequestHelper apiRequestHeaders) {
		super.delete(microservice, environment, replaceAnalyticAssetMapIdIdInSingleAnalyticAssetMapEndpoint(analyticAssetMapId), apiRequestHeaders);
	}

	public ConnectionResponse update(String microservice, String environment, String payload, String analyticAssetMapId, APIRequestHelper apiRequestHeaders) {
		return super.update(microservice, environment, replaceAnalyticAssetMapIdIdInSingleAnalyticAssetMapEndpoint(analyticAssetMapId), payload, apiRequestHeaders);
	}

	public ConnectionResponse retrieve(String microservice, String environment, APIRequestHelper apiRequestHeaders) {
		return super.retrieve(microservice, environment, getAllUserGroupsEndpoint, apiRequestHeaders);
	}

	public ConnectionResponse retrieve(String microservice, String environment, String analyticAssetMapId, APIRequestHelper apiRequestHeaders) {
		return super.retrieve(microservice, environment, replaceAnalyticAssetMapIdIdInSingleAnalyticAssetMapEndpoint(analyticAssetMapId), apiRequestHeaders);
	}

	protected String replaceAnalyticAssetMapIdIdInSingleAnalyticAssetMapEndpoint(String analyticAssetMapId) {
		String singleAnalyticAssetMapEndpoint = getOrDeleteSingleUserGroupEndpointAbstract.replace("{analyticAssetMapId}", analyticAssetMapId);
		return singleAnalyticAssetMapEndpoint;
	}
}
