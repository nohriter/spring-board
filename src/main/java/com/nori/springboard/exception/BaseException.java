package com.nori.springboard.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

	private final String code;

	public BaseException(ExceptionMessage exceptionMessage) {
		super(exceptionMessage.getMessage());
		this.code = exceptionMessage.getCode();
	}
}
