package com.nori.springboard.exception;

public class NotVerifyEmailException extends BaseException {

	public NotVerifyEmailException() {
		super(ExceptionMessage.NOT_VERIFY_EMAIL);
	}
}
