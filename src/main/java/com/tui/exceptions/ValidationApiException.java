package com.tui.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationApiException extends ApiException {
	private static final long serialVersionUID = -82921317442535369L;

	public ValidationApiException() {
		super();
	}

	public ValidationApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationApiException(String message) {
		super(message);
	}

	public ValidationApiException(Throwable cause) {
		super(cause);
	}
}