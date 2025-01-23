package com.david.apis.loancalc.model;

public class AppRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 0L;

	public AppRuntimeException() {}

	public AppRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppRuntimeException(String message) {
		super(message);
	}

	public AppRuntimeException(Throwable cause) {
		super(cause);
	}	
}