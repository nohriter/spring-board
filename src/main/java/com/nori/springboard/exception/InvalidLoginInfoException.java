package com.nori.springboard.exception;

import static com.nori.springboard.exception.ExceptionMessage.INVALID_EMAIL_OR_PASSWORD;

public class InvalidLoginInfoException extends BaseException {

	public InvalidLoginInfoException() {
		super(INVALID_EMAIL_OR_PASSWORD);
	}

}
