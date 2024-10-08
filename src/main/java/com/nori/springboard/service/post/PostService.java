package com.nori.springboard.service.post;

import com.nori.springboard.controller.post.PostRequest;
import com.nori.springboard.entity.board.Board;
import com.nori.springboard.entity.board.BoardRepository;
import com.nori.springboard.entity.category.Category;
import com.nori.springboard.entity.category.CategoryRepository;
import com.nori.springboard.entity.category.CategoryType;
import com.nori.springboard.entity.image.Image;
import com.nori.springboard.entity.image.ImageRepository;
import com.nori.springboard.entity.member.Member;
import com.nori.springboard.entity.member.MemberRepository;
import com.nori.springboard.entity.post.Post;
import com.nori.springboard.entity.post.PostRepository;
import com.nori.springboard.exception.InvalidParameterException;
import com.nori.springboard.exception.NotFoundMemberException;
import com.nori.springboard.exception.post.NotFoundPostException;
import com.nori.springboard.service.image.ImageSimpleResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

	private final MemberRepository memberRepository;
	private final CategoryRepository categoryRepository;
	private final PostRepository postRepository;
	private final BoardRepository boardRepository;
	private final ImageRepository imageRepository;

	public PostResponse postCreate(Long memberId, PostRequest request) {
		verifyPostOwner(memberId, request.getWriterId());

		Member writer = memberRepository.findById(memberId)
			.orElseThrow(NotFoundMemberException::new);

		Board board = boardRepository.findByTitle(request.getBoardTitle())
			.orElseThrow(IllegalArgumentException::new);

		Category category = categoryRepository.findByCategoryTypeAndBoard(request.getCategoryType(), board)
			.orElseThrow(IllegalArgumentException::new);

		Post post = request.toEntity(writer, category, board);

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

	@Transactional(readOnly = true)
	public Page<PostResponse> getPostPages(String boardTitle, String category, int pageNumber) {
		CategoryType categoryType = CategoryType.of(category);

		PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10, Sort.by("createdAt").descending());

		boardRepository.findByTitle(boardTitle)
			.orElseThrow(InvalidParameterException::new);

		// TODO: 8/23/24 코드 동적쿼리로 개선하기
		// 'ALL'인 경우 모든 게시글을 조회
		if (categoryType == CategoryType.ALL) {
			return postRepository.findByBoardTitle(boardTitle, pageRequest)
				.map(PostResponse::of);
		}

		return postRepository.findByBoardTitleAndCategoryType(boardTitle, categoryType, pageRequest)
			.map(PostResponse::of);
	}

	@Transactional(readOnly = true)
	public PostDetailResponse getPost(Long postId) {
		Post post = findPostById(postId);
		List<ImageSimpleResponse> images = imageRepository.findByPostId(post.getId()).stream()
			.map(image -> new ImageSimpleResponse(image.getId(), image.getUrl()))
			.toList();

		return PostDetailResponse.of(post, images);
	}

	public void verifyPostOwner(Long memberId, Long writerId) {
		if(!memberId.equals(writerId)) {
			log.error("memberId: {}, writerId: {}", memberId, writerId);
			throw new IllegalStateException("작성자가 다릅니다.");
		}
	}

	public PostResponse updatePost(Long memberId, Long postId, PostRequest request) {
		Category category = categoryRepository.findByCategoryType(request.getCategoryType())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고입니다."));

		Post post = findPostById(postId);

		verifyPostOwner(memberId, post.getWriter().getId());;

		List<String> imageIds = request.getImageIds();

		updateImagePostMapping(imageIds, post);

		post.update(category, request.getTitle(), request.getContent());

		return PostResponse.of(post);
	}

	private void updateImagePostMapping(List<String> newImageIds, Post post) {
		// 1. 기존 게시물에 매핑된 이미지 목록을 가져온다.
		List<Image> currentImages = imageRepository.findByPostId(post.getId());

		// 2. 수정 폼에서 전달된 이미지 목록과 기존 이미지 목록을 비교한다.
		Set<String> newImageIdSet = new HashSet<>(newImageIds);

		// 3. 기존 이미지 중 삭제된 이미지를 찾아 처리한다.
		for (Image currentImage : currentImages) {
			if (!newImageIdSet.contains(currentImage.getId().toString())) {
				currentImage.removePost();
			}
		}

		for (String newImageId : newImageIds) {
			boolean isAlreadyMapped = currentImages.stream()
				.anyMatch(image -> image.getId().toString().equals(newImageId));
			if (!isAlreadyMapped) {
				Image newImage = imageRepository.findById(Long.parseLong(newImageId))
					.orElseThrow(() -> new IllegalArgumentException("Invalid image ID: " + newImageId));

				newImage.registerPost(post);
				imageRepository.save(newImage);
			}
		}
	}

	public void deletePost(Long memberId, Long postId) {
		Post post = findPostById(postId);

		verifyPostOwner(memberId, post.getWriter().getId());

		postRepository.delete(post);
	}

	private Post findPostById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(NotFoundPostException::new);
	}
}
