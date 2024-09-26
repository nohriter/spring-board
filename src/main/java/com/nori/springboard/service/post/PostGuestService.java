package com.nori.springboard.service.post;

import static com.nori.springboard.service.login.PasswordManager.isPasswordMatch;

import com.nori.springboard.controller.post.PostGuestRequest;
import com.nori.springboard.entity.board.Board;
import com.nori.springboard.entity.board.BoardRepository;
import com.nori.springboard.entity.category.Category;
import com.nori.springboard.entity.category.CategoryRepository;
import com.nori.springboard.entity.image.Image;
import com.nori.springboard.entity.image.ImageRepository;
import com.nori.springboard.entity.post.Post;
import com.nori.springboard.entity.post.PostRepository;
import com.nori.springboard.exception.post.NotFoundPostException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostGuestService {

	private final CategoryRepository categoryRepository;
	private final PostRepository postRepository;
	private final BoardRepository boardRepository;
	private final ImageRepository imageRepository;

	public PostResponse postCreate(PostGuestRequest request) {
		Board board = boardRepository.findByTitle(request.getBoardTitle())
			.orElseThrow(IllegalArgumentException::new);

		Category category = categoryRepository.findByCategoryTypeAndBoard(request.getCategoryType(), board)
			.orElseThrow(IllegalArgumentException::new);

		Post post = request.toEntity(category, board);

		imagePostMapping(request.getImageIds(), post);

		return PostResponse.of(postRepository.save(post));
	}

	private void imagePostMapping(List<String> imageIds, Post post) {
		if (imageIds != null && !imageIds.isEmpty()) {
			for (String imageId : imageIds) {
				Image image = imageRepository.findById(Long.parseLong(imageId))
					.orElseThrow(() -> new IllegalArgumentException("Invalid image ID: " + imageId));

				image.registerPost(post);
				imageRepository.save(image);
			}
		}
	}

	public void deletePost(Long postId, String password) {
			Post post = postRepository.findById(postId)
				.orElseThrow(NotFoundPostException::new);

			isPasswordMatch(password, post.getGuestPassword());

			post.delete();
	}

	public Post validatePostExists(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(NotFoundPostException::new);
	}

}
