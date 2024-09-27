package com.nori.springboard.service.image;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageSimpleResponse {

	private Long id;
	private String url;

	public ImageSimpleResponse(Long id, String url) {
		this.id = id;
		this.url = url;
	}
}
