package com.nori.springboard.service.post;

import com.nori.springboard.controller.post.PostRequest;
import com.nori.springboard.entity.category.Category;
import com.nori.springboard.entity.category.CategoryRepository;
import com.nori.springboard.entity.category.CategoryType;
import com.nori.springboard.entity.member.Member;
import com.nori.springboard.entity.member.MemberRepository;
import com.nori.springboard.entity.post.Post;
import com.nori.springboard.entity.post.PostRepository;
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

	public PostResponse postCreate(Long memberId, PostRequest request) {
		verifyPostOwner(memberId, request.getWriterId());

		Member writer = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException());

		Category category = categoryRepository.findByCategoryType(request.getCategoryType())
			.orElseThrow(() -> new IllegalArgumentException());

		Post post = request.toEntity(writer, category);

		return PostResponse.of(postRepository.save(post));
	}

	@Transactional(readOnly = true)
	public Page<PostResponse> getPostPages(String category, int pageNumber) {
		CategoryType categoryType = CategoryType.of(category);
		PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10, Sort.by("createdAt").descending());

		// TODO: 8/23/24 코드 개선하기
		// 'ALL'인 경우 모든 게시글을 조회
		if (categoryType == CategoryType.ALL) {
			return postRepository.findAll(pageRequest).map(PostResponse::of);
		}

		return postRepository.findByCategory_CategoryType(categoryType, pageRequest)
			.map(PostResponse::of);
	}

	@Transactional(readOnly = true)
	public PostResponse getPost(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(IllegalArgumentException::new);

		return PostResponse.of(post);
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

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글니다."));

		verifyPostOwner(memberId, post.getWriter().getId());;

		post.update(category, request.getTitle(), request.getContent());

		return PostResponse.of(post);
	}

	public void deletePost(Long memberId, Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글니다."));

		verifyPostOwner(memberId, post.getWriter().getId());;

		postRepository.delete(post);
	}
}
