package com.nori.springboard.service;

import com.nori.springboard.entity.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

	private Long id;
	private String categoryName;
	private String title;
	private String content;
	private Long writerId;
	private String writerName;
	private LocalDateTime createAt;
	private LocalDateTime updatedAt;
	private Long hitCount;

	@Builder
	public PostResponse(Long id, String categoryName, String title, String content, Long writerId,
		String writerName, LocalDateTime createAt, LocalDateTime updatedAt, Long hitCount) {
		this.id = id;
		this.categoryName = categoryName;
		this.title = title;
		this.content = content;
		this.writerId = writerId;
		this.writerName = writerName;
		this.createAt = createAt;
		this.updatedAt = updatedAt;
		this.hitCount = hitCount;
	}

	public static PostResponse of(Post post) {
		return PostResponse.builder()
			.id(post.getId())
			.categoryName(post.getCategory().getCategoryType().name())
			.title(post.getTitle())
			.content(post.getContent())
			.writerId(post.getWriter().getId())
			.writerName(post.getWriter().getNickname())
			.createAt(post.getCreatedAt())
			.updatedAt(post.getUpdatedAt())
			.hitCount(post.getViewsCount())
			.build();
	}
}
