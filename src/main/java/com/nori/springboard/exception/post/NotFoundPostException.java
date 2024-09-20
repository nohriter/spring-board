package com.nori.springboard.exception.post;

import static com.nori.springboard.exception.ExceptionMessage.NOT_FOUND_POST;

import com.nori.springboard.exception.BaseException;

public class NotFoundPostException extends BaseException {

	public NotFoundPostException() {
		super(NOT_FOUND_POST);
	}
}
