package com.nori.springboard.service.image;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageUploadRequest {

	private String url;

	private String key;

	private Long postId;

	@Builder
	private ImageUploadRequest(String url, String key, Long postId) {
		this.url = url;
		this.key = key;
		this.postId = postId;
	}

}
