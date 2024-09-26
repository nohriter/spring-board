package com.nori.springboard.service;

import com.nori.springboard.exception.InvalidFileException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileValidationService {

	private static final String[] ALLOWED_IMAGE_EXTENSIONS = {"png", "jpeg", "jpg"};

	public void validateFile(MultipartFile file) {
		String contentType = file.getContentType();

		if (contentType != null && !contentType.startsWith("image/")) {
			throw new InvalidFileException();
		}
		log.info("file contentType: {}", contentType);

		// 파일 확장자 검사
		String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
		if (extension == null) {
			throw new InvalidFileException();
		}
		log.info("file extension: {}", extension);
		// 파일 유형에 따른 확장자 제한
		if (!isExtensionAllowed(extension.toLowerCase(), ALLOWED_IMAGE_EXTENSIONS)) {
			throw new InvalidFileException();
		}
	}

	private boolean isExtensionAllowed(String extension, String[] allowedExtensions) {
		return Arrays.stream(allowedExtensions).anyMatch(extension::equals);
	}

}
