package com.qio.lib.apiHelpers;

import com.qio.model.assetType.AssetType;

public class APIRequestHelper {
	private String acceptType;
	private String contentType;
	private String userName;
	private String password;
	private String grant_type;
	private String scope;
	private String oauthMicroservice;
	private Boolean fetchNewAccessToken;

	// By default, a new Access Token will be fetched whenever a new object for
	// APIHeaders gets instantiated.
	public APIRequestHelper(String acceptType, String contentType, String userName, String password, String grant_type, String scope,
			String oauthMicroservice) {
		this.acceptType = acceptType;
		this.contentType = contentType;
		this.userName = userName;
		this.password = password;
		this.grant_type = grant_type;
		this.scope = scope;
		this.fetchNewAccessToken = true;
		this.oauthMicroservice = oauthMicroservice;
	}

	public APIRequestHelper(String acceptType, String contentType, String userName, String password, String oauthMicroservice) {
		this(acceptType, contentType, userName, password, "password", "openid,profile,token", oauthMicroservice);
	}

	public APIRequestHelper(String userName, String password, String oauthMicroservice) {
		this("application/json", "application/json", userName, password, oauthMicroservice);
	}

	public APIRequestHelper(APIRequestHelper apiRequestHelper) {
		this(apiRequestHelper.getAcceptType(), apiRequestHelper.getContentType(), apiRequestHelper.getUserName(), apiRequestHelper.getPassword(),
				apiRequestHelper.getGrant_type(), apiRequestHelper.getScope(), apiRequestHelper.getOauthMicroservice());
	}

	public String getAcceptType() {
		return acceptType;
	}

	public void setAcceptType(String acceptType) {
		this.acceptType = acceptType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUserName() {
		return userName;
	}

	// Whenever the username is changed, a new access token should be fetched.
	public void setUserName(String userName) {
		this.userName = userName;
		this.fetchNewAccessToken = true;
	}

	public String getPassword() {
		return password;
	}

	// Whenever the password is changed, a new access token should be fetched.
	public void setPassword(String password) {
		this.password = password;
		this.fetchNewAccessToken = true;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getOauthMicroservice() {
		return oauthMicroservice;
	}

	public void setOauthMicroservice(String oauthMicroservice) {
		this.oauthMicroservice = oauthMicroservice;
	}

	public Boolean getFetchNewAccessToken() {
		return fetchNewAccessToken;
	}

	public void setFetchNewAccessToken(Boolean fetchNewAccessToken) {
		this.fetchNewAccessToken = fetchNewAccessToken;
	}
}
