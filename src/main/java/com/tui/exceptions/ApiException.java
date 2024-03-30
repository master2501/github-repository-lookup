package com.tui.exceptions;

public abstract class ApiException extends Exception {
	private static final long serialVersionUID = -8670121268019169960L;

	public ApiException() {
		super();
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}
}
