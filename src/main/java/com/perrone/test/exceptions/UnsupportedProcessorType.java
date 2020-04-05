package com.perrone.test.exceptions;

public class UnsupportedProcessorType extends RuntimeException {

	public UnsupportedProcessorType(String message) {
		super(message);
	}

	public UnsupportedProcessorType(Throwable cause) {
		super(cause);
	}

	public UnsupportedProcessorType(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedProcessorType(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
