package com.nori.springboard.service;

import com.nori.springboard.controller.PostWriteRequest;
import com.nori.springboard.entity.Category;
import com.nori.springboard.entity.CategoryRepository;
import com.nori.springboard.entity.CategoryType;
import com.nori.springboard.entity.Member;
import com.nori.springboard.entity.MemberRepository;
import com.nori.springboard.entity.Post;
import com.nori.springboard.entity.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

	private final MemberRepository memberRepository;
	private final CategoryRepository categoryRepository;
	private final PostRepository postRepository;

	public PostResponse postCreate(Long memberId, PostWriteRequest request) {
		if(memberId != request.getWriterId()) {
			throw new IllegalStateException("작성자가 다릅니다.");
		}

		Member writer = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException());

		Category category = categoryRepository.findByCategoryType(request.getCategoryType())
			.orElseThrow(() -> new IllegalArgumentException());

		Post post = request.toEntity(writer, category);

		return PostResponse.of(postRepository.save(post));
	}

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

	public PostResponse getPost(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(IllegalArgumentException::new);

		return PostResponse.of(post);
	}
}
