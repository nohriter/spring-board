package com.nori.springboard.controller.image;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageUploadFailResponse {

	private ErrorDetail error;

	@Getter
	@Setter
	@NoArgsConstructor
	private static class ErrorDetail {

		private String message;
	}
}
