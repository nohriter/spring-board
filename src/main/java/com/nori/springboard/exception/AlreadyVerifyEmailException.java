package com.nori.springboard.exception;

public class AlreadyVerifyEmailException extends BaseException {

	public AlreadyVerifyEmailException() {
		super(ExceptionMessage.ALREADY_VERIFY_EMAIL);
	}
}
