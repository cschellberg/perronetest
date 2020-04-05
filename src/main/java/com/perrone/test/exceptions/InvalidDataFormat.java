package com.perrone.test.exceptions;

public class InvalidDataFormat extends RuntimeException {

	public InvalidDataFormat(String message) {
		super(message);
	}

	public InvalidDataFormat(Throwable cause) {
		super(cause);
	}

	public InvalidDataFormat(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDataFormat(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
