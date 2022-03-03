package com.poc.es.utils;

public class AppServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AppServiceException(String message) {
		super(message, null, true, false);
	}

	public AppServiceException(String message, Throwable cause) {
		super(message, cause, true, false);
	}
}
