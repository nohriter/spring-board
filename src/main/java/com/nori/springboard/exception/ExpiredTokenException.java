package com.nori.springboard.exception;

import static com.nori.springboard.exception.ExceptionMessage.*;

public class ExpiredTokenException extends BaseException {

	public ExpiredTokenException() {
		super(EXPIRED_TOKEN);
	}
}
