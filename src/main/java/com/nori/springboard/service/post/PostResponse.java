package com.nori.springboard.service.post;

import com.nori.springboard.common.TimeFormatter;
import com.nori.springboard.entity.post.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

	private Long id;
	private String categoryName;
	private String categoryEngName;
	private String title;
	private String content;
	private Long writerId;
	private String writerName;
	private String createdAt;
	private String updatedAt;
	private String updatedDateTime;
	private Long viewCount;

	@Builder
	private PostResponse(Long id, String categoryName, String categoryEngName, String title, String content, Long writerId,
		String writerName, String createdAt, String updatedAt, String updatedDateTime, Long viewCount) {
		this.id = id;
		this.categoryName = categoryName;
		this.categoryEngName = categoryEngName;
		this.title = title;
		this.content = content;
		this.writerId = writerId;
		this.writerName = writerName;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.updatedDateTime = updatedDateTime;
		this.viewCount = viewCount;
	}

	public static PostResponse of(Post post) {
		return PostResponse.builder()
			.id(post.getId())
			.categoryName(post.getCategory().getCategoryType().getValue())
			.categoryEngName(post.getCategory().getCategoryType().name().toLowerCase())
			.title(post.getTitle())
			.content(post.getContent())
			.writerId(post.getWriter().getId())
			.writerName(post.getWriter().getNickname())
			.createdAt(TimeFormatter.toTodayOrNot(post.getCreatedAt()))
			.updatedAt(TimeFormatter.toTodayOrNot(post.getUpdatedAt()))
			.updatedDateTime(TimeFormatter.toDateTime(post.getUpdatedAt()))
			.viewCount(post.getViewCount())
			.build();
	}
}
