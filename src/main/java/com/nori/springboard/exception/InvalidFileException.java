package com.nori.springboard.exception;

import static com.nori.springboard.exception.ExceptionMessage.INVALID_FILE;

public class InvalidFileException extends BaseException {

	public InvalidFileException() {
		super(INVALID_FILE);
	}
}
