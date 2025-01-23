package com.david.apis.loancalc.model;

public class GenericErrorResponse {
	private String path;
	private int errorCode;
	private String desciption;
	
	public GenericErrorResponse() {
	}
	
	/**
	 * 
	 * @param errorCode
	 * @param description
	 */
	public GenericErrorResponse(int errorCode, String description) {
		this.errorCode = errorCode;
		this.desciption = description;
	}

	/**
	 * 
	 * @param path
	 * @param errorCode
	 * @param description
	 */
	public GenericErrorResponse(String path, int errorCode, String description) {
		this.path = path;
		this.errorCode = errorCode;
		this.desciption = description;
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
