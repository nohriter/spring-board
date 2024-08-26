package com.nori.springboard.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {

	ALL("전체"),
	GENERAL("일반"),
	QUESTION("질문"),
	INFORMATION("정보");

	private final String value;

	public static CategoryType of(String name) {
		for (CategoryType categoryType : values()) {
			if (categoryType.name().equalsIgnoreCase(name)) {
				return categoryType;
			}
		}
		throw new IllegalArgumentException("Unknown category: " + name);
	}
}
