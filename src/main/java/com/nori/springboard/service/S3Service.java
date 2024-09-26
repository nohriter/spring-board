package com.nori.springboard.service;

import com.nori.springboard.controller.image.ImageUploadResponse;
import com.nori.springboard.controller.image.S3ImageUploadResponse;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

	private static final String BUCKET = "nohriter-gesipan";
	private static final String prefix = "public/";

	private final S3Template s3Template;
	private final FileValidationService validationService;

	@Transactional
	public S3ImageUploadResponse upload(MultipartFile image) throws IOException {
		validationService.validateFile(image);

		String originalName = image.getOriginalFilename();

		String extension = StringUtils.getFilenameExtension(originalName);

		String imageKey = createImageKey(extension);

		S3Resource s3Resource = s3Template.upload(BUCKET, imageKey, image.getInputStream(), ObjectMetadata.builder().contentType(extension).build());

		String imageUrl = s3Resource.getURL().toString();

		return S3ImageUploadResponse.of(imageUrl, imageKey, originalName, extension);
	}

	private static String createImageKey(String extension) {
		return prefix + UUID.randomUUID() + "." + extension;
	}

}
