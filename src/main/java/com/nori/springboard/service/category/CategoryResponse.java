package com.nori.springboard.service.category;


import com.nori.springboard.entity.category.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponse {

	private String name;        // URL 파라미터로 사용할 값
	private String displayName; // 사용자에게 보여질 값

	@Builder
	private CategoryResponse(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}

	public static CategoryResponse of(Category category) {
		return CategoryResponse.builder()
			.name(category.getCategoryType().name().toLowerCase())
			.displayName(category.getCategoryType().getValue())
			.build();
	}
}
