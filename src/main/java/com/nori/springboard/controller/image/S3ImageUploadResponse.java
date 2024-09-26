package com.nori.springboard.controller.image;

import com.nori.springboard.entity.image.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class S3ImageUploadResponse {

	private String url;
	private String key;
	private String originalName;
	private String fileExtension;

	private S3ImageUploadResponse(String url, String key, String originalName,
		String fileExtension) {
		this.url = url;
		this.key = key;
		this.originalName = originalName;
		this.fileExtension = fileExtension;
	}

	public static S3ImageUploadResponse of(String url, String key, String originalName, String fileExtension) {
		return new S3ImageUploadResponse(url, key, originalName, fileExtension);
	}

	public Image toEntity() {
		return Image.builder()
			.url(url)
			.s3Key(key)
			.originalName(originalName)
			.fileExtension(fileExtension)
			.build();
	}
}
