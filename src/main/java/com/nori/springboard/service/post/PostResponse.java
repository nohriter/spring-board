package com.nori.springboard.service.post;

import com.nori.springboard.common.TimeFormatter;
import com.nori.springboard.entity.post.Post;
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
	private String guestNickname;

	@Builder
	private PostResponse(Long id, String categoryName, String categoryEngName, String title, String content, Long writerId,
		String writerName, String createdAt, String updatedAt, String updatedDateTime, Long viewCount, String guestNickname) {
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
		this.guestNickname = guestNickname;
	}

	public static PostResponse of(Post post) {
		return PostResponse.builder()
			.id(post.getId())
			.categoryName(post.getCategory().getCategoryType().getValue())
			.categoryEngName(post.getCategory().getCategoryType().name().toLowerCase())
			.title(post.getTitle())
			.content(post.getContent())
			.writerId(post.getWriter() != null ? post.getWriter().getId() : null)
			.writerName(post.getWriter() != null ? post.getWriter().getNickname() : null)
			.guestNickname(post.getGuestNickname())
			.createdAt(TimeFormatter.toTodayOrNot(post.getCreatedAt()))
			.updatedAt(TimeFormatter.toTodayOrNot(post.getUpdatedAt()))
			.updatedDateTime(TimeFormatter.toDateTime(post.getUpdatedAt()))
			.viewCount(post.getViewCount())
			.build();
	}

}
