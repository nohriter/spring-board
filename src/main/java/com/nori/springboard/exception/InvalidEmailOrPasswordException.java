package com.nori.springboard.exception;

import static com.nori.springboard.exception.ExceptionMessage.INVALID_EMAIL_OR_PASSWORD;

public class InvalidEmailOrPasswordException extends BaseException {

	public InvalidEmailOrPasswordException() {
		super(INVALID_EMAIL_OR_PASSWORD);
	}

}
