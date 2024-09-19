package com.nori.springboard.entity.category;

import com.nori.springboard.exception.InvalidParameterException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {

	ALL("전체"),
	NOTICE("공지"),
	MUST("필독"),
	INFORMATION("정보"),
	QUESTION("질문"),
	FUNNY("유머"),
	FOOD("음식");

	private final String value;

	public static CategoryType of(String name) {
		return Arrays.stream(values())
			.filter(categoryType -> categoryType.name().equalsIgnoreCase(name)) // 대소문자 구분 없이 처리
			.findFirst()
			.orElseThrow(InvalidParameterException::new);
	}
}
