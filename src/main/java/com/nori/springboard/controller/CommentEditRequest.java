package com.nori.springboard.controller;


import lombok.Data;

@Data
public class CommentEditRequest {

	private Long commentId;
	private String content;
	private Long postId;

}
