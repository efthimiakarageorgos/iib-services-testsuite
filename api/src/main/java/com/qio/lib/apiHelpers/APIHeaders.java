package com.qio.lib.apiHelpers;

public class APIHeaders {
	private String acceptType;
	private String contentType;
	private String userName;
	private String password;
	private String grant_type;
	private String scope;
	private String envRuntime;
	private Boolean fetchNewAccessToken;

	// By default, a new Access Token will be fetched whenever a new object for
	// APIHeaders gets instantiated.
	public APIHeaders(String acceptType, String contentType, String userName, String password, String grant_type, String scope, String envRuntime) {
		this.acceptType = acceptType;
		this.contentType = contentType;
		this.userName = userName;
		this.password = password;
		this.grant_type = grant_type;
		this.scope = scope;
		this.fetchNewAccessToken = true;
		this.envRuntime = envRuntime;
	}

	public APIHeaders(String acceptType, String contentType, String userName, String password, String envRuntime) {
		this(acceptType, contentType, userName, password, "password", "openid,profile,token", envRuntime);
	}

	public APIHeaders(String userName, String password, String envRuntime) {
		this("application/json", "application/json", userName, password, envRuntime);
	}

	public APIHeaders(String userName, String password) {
		this("application/json", "application/json", userName, password, "dev");
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

	public String getEnvRuntime() {
		return envRuntime;
	}

	public void setEnvRuntime(String envRuntime) {
		this.envRuntime = envRuntime;
	}

	public Boolean getFetchNewAccessToken() {
		return fetchNewAccessToken;
	}

	public void setFetchNewAccessToken(Boolean fetchNewAccessToken) {
		this.fetchNewAccessToken = fetchNewAccessToken;
	}
}
