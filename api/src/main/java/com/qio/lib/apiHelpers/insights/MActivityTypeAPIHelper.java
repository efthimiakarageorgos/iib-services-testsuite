package com.qio.lib.apiHelpers.insights;

import com.qio.lib.apiHelpers.APIRequestHelper;
import com.qio.lib.common.MBaseAPIHelper;
import com.qio.lib.connection.ConnectionResponse;

public class MActivityTypeAPIHelper extends MBaseAPIHelper {
	private final String createOrUpdateActivityTypeEndpoint = "/activitytypes";
	private final String getOrDeleteSingleActivityTypeEndpointAbstract = "/activitytypes/{activityTypeId}";
	private final String getAllActivityTypesEndpoint = "/activitytypes";

	public String getGetOrDeleteSingleActivityTypeEndpointAbstract() {
		return getOrDeleteSingleActivityTypeEndpointAbstract;
	}

	public ConnectionResponse create(String microservice, String environment, String payload, APIRequestHelper apiRequestHelper) {
		return super.create(microservice, environment, createOrUpdateActivityTypeEndpoint, payload, apiRequestHelper);
	}

	public void delete(String microservice, String environment, String ActivityTypeId, APIRequestHelper apiRequestHelper) {
		super.delete(microservice, environment, replaceActivityTypeIdInSingleActivityTypeEndpoint(ActivityTypeId), apiRequestHelper);
	}

	public ConnectionResponse update(String microservice, String environment, String payload, String ActivityTypeId,
			APIRequestHelper apiRequestHelper) {
		return super.update(microservice, environment, replaceActivityTypeIdInSingleActivityTypeEndpoint(ActivityTypeId), payload, apiRequestHelper);
	}

	public ConnectionResponse retrieve(String microservice, String environment, APIRequestHelper apiRequestHelper) {
		return super.retrieve(microservice, environment, getAllActivityTypesEndpoint, apiRequestHelper);
	}

	public ConnectionResponse retrieve(String microservice, String environment, String ActivityTypeId, APIRequestHelper apiRequestHelper) {
		return super.retrieve(microservice, environment, replaceActivityTypeIdInSingleActivityTypeEndpoint(ActivityTypeId), apiRequestHelper);
	}

	protected String replaceActivityTypeIdInSingleActivityTypeEndpoint(String ActivityTypeId) {
		String singleActivityTypeEndpoint = getOrDeleteSingleActivityTypeEndpointAbstract.replace("{activityTypeId}", ActivityTypeId);
		return singleActivityTypeEndpoint;
	}
}
