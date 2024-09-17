package com.nori.springboard.exception;


import static com.nori.springboard.exception.ExceptionMessage.NOT_FOUND_MEMBER;

public class NotFoundMemberException extends BaseException {

	public NotFoundMemberException() {
		super(NOT_FOUND_MEMBER);
	}
}
