package com.nori.springboard.service.post;

import static com.nori.springboard.service.login.PasswordManager.isPasswordMatch;

import com.nori.springboard.controller.post.PostGuestRequest;
import com.nori.springboard.entity.board.Board;
import com.nori.springboard.entity.board.BoardRepository;
import com.nori.springboard.entity.category.Category;
import com.nori.springboard.entity.category.CategoryRepository;
import com.nori.springboard.entity.post.Post;
import com.nori.springboard.entity.post.PostRepository;
import com.nori.springboard.exception.post.NotFoundPostException;
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

	public PostResponse postCreate(PostGuestRequest request) {
		Board board = boardRepository.findByTitle(request.getBoardTitle())
			.orElseThrow(IllegalArgumentException::new);

		Category category = categoryRepository.findByCategoryTypeAndBoard(request.getCategoryType(), board)
			.orElseThrow(IllegalArgumentException::new);

		Post post = request.toEntity(category, board);

		return PostResponse.of(postRepository.save(post));
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

	public void validatePostAndCategory(Long postId, String category) {
		postRepository.findById(postId)
			.orElseThrow(NotFoundPostException::new);
	}
}
