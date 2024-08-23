package com.nori.springboard.service;

import com.nori.springboard.controller.PostWriteRequest;
import com.nori.springboard.entity.Category;
import com.nori.springboard.entity.CategoryRepository;
import com.nori.springboard.entity.Member;
import com.nori.springboard.entity.MemberRepository;
import com.nori.springboard.entity.Post;
import com.nori.springboard.entity.PostRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
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
}
