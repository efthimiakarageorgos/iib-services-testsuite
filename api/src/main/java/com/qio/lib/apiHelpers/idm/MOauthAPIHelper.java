package com.qio.lib.apiHelpers.idm;

import com.qio.lib.apiHelpers.APIRequestHelper;
import com.qio.lib.common.MBaseAPIHelper;

public class MOauthAPIHelper extends MBaseAPIHelper {
	private final String oauthEndpoint = "/oauth2/token";

	public void authenticateUsingOauth(String microservice, String environment, APIRequestHelper apiRequestHelper) {
		super.authenticateUsingOauth(microservice, environment, oauthEndpoint, apiRequestHelper);
	}
}
