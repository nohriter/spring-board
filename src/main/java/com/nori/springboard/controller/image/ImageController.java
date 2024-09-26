package com.nori.springboard.controller.image;

import com.nori.springboard.service.S3Service;

import com.nori.springboard.service.image.ImageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController {

	private final S3Service s3Service;
	private final ImageService imageService;

	@PostMapping("/image/upload")
	public ImageUploadResponse imageUpload(@RequestParam("upload") MultipartFile file)
		throws IOException {

		S3ImageUploadResponse s3Response = s3Service.upload(file);
		return imageService.saveImage(s3Response);
	}
}

