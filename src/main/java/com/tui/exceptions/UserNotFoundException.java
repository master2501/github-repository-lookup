package com.tui.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Signals a user not found error
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends ApiException {
	private static final long serialVersionUID = -4856516569308144921L;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}
}
