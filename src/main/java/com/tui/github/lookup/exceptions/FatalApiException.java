package com.tui.github.lookup.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FatalApiException extends ApiException {
	private static final long serialVersionUID = 2836624960327444098L;

	public FatalApiException() {
		super();
	}

	public FatalApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public FatalApiException(String message) {
		super(message);
	}

	public FatalApiException(Throwable cause) {
		super(cause);
	}
}
