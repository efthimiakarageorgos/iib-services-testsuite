package com.qio.lib.exception;

public class ServerResponse {
	private String timestamp;
	private int status;
	private String error;
	private String exception;
	private String message;
	private String path;
	
	public String getTimestamp() {
		return timestamp;
	}
	public int getStatus() {
		return status;
	}
	public String getError() {
		return error;
	}
	public String getException() {
		return exception;
	}
	public String getMessage() {
		return message;
	}
	public String getPath() {
		return path;
	}
}
