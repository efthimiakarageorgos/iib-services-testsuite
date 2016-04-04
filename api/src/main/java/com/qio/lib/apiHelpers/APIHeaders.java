package com.qio.lib.apiHelpers;

public class APIHeaders {
	private String acceptType;
	private String contentType;
	private String userName;
	private String password;
	
	public APIHeaders(String acceptType, String contentType, String userName, String password){
		this.acceptType = acceptType;
		this.contentType = contentType;
		this.userName = userName;
		this.password = password;
	}
	
	// Default Accept Type and Content-Type
	public APIHeaders(String userName, String password){
		this("application/json", "application/json", userName, password);
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
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
