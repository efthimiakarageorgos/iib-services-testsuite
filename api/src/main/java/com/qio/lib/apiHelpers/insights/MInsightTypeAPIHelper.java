package com.qio.lib.apiHelpers.insights;

import com.qio.lib.apiHelpers.APIRequestHelper;
import com.qio.lib.common.MBaseAPIHelper;
import com.qio.lib.connection.ConnectionResponse;

public class MInsightTypeAPIHelper extends MBaseAPIHelper {
	private final String createOrUpdateInsightTypeEndpoint = "/insighttypes";
	private final String getOrDeleteSingleInsightTypeEndpointAbstract = "/insighttypes/{insightTypeId}";
	private final String getAllInsightTypesEndpoint = "/insighttypes";

	public String getGetOrDeleteSingleInsightTypeEndpointAbstract() {
		return getOrDeleteSingleInsightTypeEndpointAbstract;
	}

	public ConnectionResponse create(String microservice, String environment, String payload, APIRequestHelper apiRequestHeaders) {
		return super.create(microservice, environment, createOrUpdateInsightTypeEndpoint, payload, apiRequestHeaders);
	}

	public void delete(String microservice, String environment, String InsightTypeId, APIRequestHelper apiRequestHeaders) {
		super.delete(microservice, environment, replaceInsightTypeIdInSingleInsightTypeEndpoint(InsightTypeId), apiRequestHeaders);
	}

	public ConnectionResponse update(String microservice, String environment, String payload, String InsightTypeId, APIRequestHelper apiRequestHeaders) {
		return super.update(microservice, environment, replaceInsightTypeIdInSingleInsightTypeEndpoint(InsightTypeId), payload, apiRequestHeaders);
	}

	public ConnectionResponse retrieve(String microservice, String environment, APIRequestHelper apiRequestHeaders) {
		return super.retrieve(microservice, environment, getAllInsightTypesEndpoint, apiRequestHeaders);
	}

	public ConnectionResponse retrieve(String microservice, String environment, String InsightTypeId, APIRequestHelper apiRequestHeaders) {
		return super.retrieve(microservice, environment, replaceInsightTypeIdInSingleInsightTypeEndpoint(InsightTypeId), apiRequestHeaders);
	}

	protected String replaceInsightTypeIdInSingleInsightTypeEndpoint(String InsightTypeId) {
		String singleInsightTypeEndpoint = getOrDeleteSingleInsightTypeEndpointAbstract.replace("{InsightTypeId}", InsightTypeId);
		return singleInsightTypeEndpoint;
	}
}
