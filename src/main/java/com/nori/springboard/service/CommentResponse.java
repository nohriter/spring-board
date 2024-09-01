package com.nori.springboard.service;

import com.nori.springboard.entity.Comment;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {

	private Long id;
	private String content;
	private Long writerId;
	private String writerNickname;
	private String parentsCommentNickname;
	private String createdAt;
	private boolean isReply;
	private boolean isDeleted;
	private List<CommentResponse> replies;

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Builder
	public CommentResponse(Long id, String content, Long writerId, String writerNickname,
		String createdAt, boolean isReply, boolean isDeleted,List<CommentResponse> replies, String parentsCommentNickname) {
		this.id = id;
		this.content = content;
		this.writerId = writerId;
		this.writerNickname = writerNickname;
		this.createdAt = createdAt;
		this.isReply = isReply;
		this.isDeleted = isDeleted;
		this.replies = replies;
		this.parentsCommentNickname = parentsCommentNickname;
	}

	public static CommentResponse of(Comment comment, List<CommentResponse> replies) {
		return CommentResponse.builder()
			.id(comment.getId())
			.content(comment.getContent())
			.writerId(comment.getWriter().getId())
			.writerNickname(comment.getWriter().getNickname())
			.createdAt(comment.getCreatedAt().format(formatter))
			.isReply(comment.isReply())
			.isDeleted(comment.isDeleted())
			.replies(replies != null ? replies : new ArrayList<>())
			.parentsCommentNickname(comment.getParentNickname())
			.build();
	}

	public static CommentResponse of(Comment comment) {
		return of(comment, null);
	}


}
