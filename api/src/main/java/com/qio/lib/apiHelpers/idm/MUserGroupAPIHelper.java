package com.qio.lib.apiHelpers.idm;

import com.qio.lib.apiHelpers.APIRequestHelper;
import com.qio.lib.common.MBaseAPIHelper;
import com.qio.lib.connection.ConnectionResponse;

public class MUserGroupAPIHelper extends MBaseAPIHelper {
	private final String createOrUpdateUserGroupEndpoint = "/groups";
	private final String getOrDeleteSingleUserGroupEndpointAbstract = "/groups/{userGroupId}";
	private final String getAllUserGroupsEndpoint = "/groups";

	public String getGetOrDeleteSingleUserGroupEndpointAbstract() {
		return getOrDeleteSingleUserGroupEndpointAbstract;
	}

	public ConnectionResponse create(String microservice, String environment, String payload, APIRequestHelper apiRequestHeaders) {
		return super.create(microservice, environment, createOrUpdateUserGroupEndpoint, payload, apiRequestHeaders);
	}

	public void delete(String microservice, String environment, String userGroupId, APIRequestHelper apiRequestHeaders) {
		super.delete(microservice, environment, replaceUserGroupIdInSingleUserGroupEndpoint(userGroupId), apiRequestHeaders);
	}

	public ConnectionResponse update(String microservice, String environment, String payload, String userGroupId, APIRequestHelper apiRequestHeaders) {
		return super.update(microservice, environment, replaceUserGroupIdInSingleUserGroupEndpoint(userGroupId), payload, apiRequestHeaders);
	}

	public ConnectionResponse retrieve(String microservice, String environment, APIRequestHelper apiRequestHeaders) {
		return super.retrieve(microservice, environment, getAllUserGroupsEndpoint, apiRequestHeaders);
	}

	public ConnectionResponse retrieve(String microservice, String environment, String userGroupId, APIRequestHelper apiRequestHeaders) {
		return super.retrieve(microservice, environment, replaceUserGroupIdInSingleUserGroupEndpoint(userGroupId), apiRequestHeaders);
	}

	protected String replaceUserGroupIdInSingleUserGroupEndpoint(String userGroupId) {
		String singleUserGroupEndpoint = getOrDeleteSingleUserGroupEndpointAbstract.replace("{userGroupId}", userGroupId);
		return singleUserGroupEndpoint;
	}
}
