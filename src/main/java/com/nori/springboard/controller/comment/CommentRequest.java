package com.nori.springboard.controller.comment;

import com.nori.springboard.entity.comment.Comment;
import com.nori.springboard.entity.member.Member;
import com.nori.springboard.entity.post.Post;
import lombok.Data;

@Data
public class CommentRequest {

	private Long postId;

	private Long parentCommentId;

	private String content;

	private Boolean isReply;

	public Comment toEntity(Member member, Post post, Comment comment, String parentNickname) {
		return Comment.builder()
			.content(content)
			.post(post)
			.writer(member)
			.parent(comment)
			.isReply(isReply)
			.parentNickname(parentNickname)
			.build();
	}

	public Comment toEntity(Member member, Post post, Comment comment) {
		return Comment.builder()
			.content(content)
			.post(post)
			.writer(member)
			.parent(comment)
			.isReply(isReply)
			.build();
	}
}
