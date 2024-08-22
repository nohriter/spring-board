package com.nori.springboard.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {

	GENERAL("일반"),
	QUESTION("질문"),
	INFORMATION("정보");

	private final String value;
}
