package com.nori.springboard.controller.post;

import com.nori.springboard.entity.category.Category;
import com.nori.springboard.entity.category.CategoryType;
import com.nori.springboard.entity.member.Member;
import com.nori.springboard.entity.post.Post;
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
