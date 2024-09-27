package com.nori.springboard.service.image;

import com.nori.springboard.controller.image.ImageUploadResponse;
import com.nori.springboard.controller.image.S3ImageUploadResponse;
import com.nori.springboard.entity.image.Image;
import com.nori.springboard.entity.image.ImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;

	public ImageUploadResponse saveImage(S3ImageUploadResponse request) {
		Image image = imageRepository.save(request.toEntity());
		return ImageUploadResponse.of(image);
	}

	public List<Long> getImageIds(Long postId) {
		return imageRepository.findByPostId(postId).stream()
			.map(Image::getId)
			.toList();
	}

}
