package com.nori.springboard.controller;

import com.nori.springboard.entity.Category;
import com.nori.springboard.entity.CategoryType;
import com.nori.springboard.entity.Member;
import com.nori.springboard.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostRequest {

	private CategoryType categoryType;
	private String title;
	private String content;
	private Long writerId;

	public Post toEntity(Member writer, Category category) {
		return Post.builder()
			.writer(writer)
			.category(category)
			.title(title)
			.content(content)
			.build();
	}
}
