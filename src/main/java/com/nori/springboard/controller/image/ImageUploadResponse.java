package com.nori.springboard.controller.image;

import com.nori.springboard.entity.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageUploadResponse {

	private String url;
	private Long imageId;

	@Builder
	private ImageUploadResponse(String url, Long imageId) {
		this.url = url;
		this.imageId = imageId;
	}

	public static ImageUploadResponse of(Image image) {
		return ImageUploadResponse.builder()
			.url(image.getUrl())
			.imageId(image.getId())
			.build();
	}
}
