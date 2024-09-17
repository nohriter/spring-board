package com.nori.springboard.exception;

public class InvalidParameterException extends BaseException {

	public InvalidParameterException() {
		super(ExceptionMessage.INVALID_PARAMETER);
	}
}
