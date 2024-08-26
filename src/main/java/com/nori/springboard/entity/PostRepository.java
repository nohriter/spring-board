package com.nori.springboard.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	// 카테고리별로 Post를 페이징 처리하여 조회
	Page<Post> findByCategory_CategoryType(CategoryType categoryType, Pageable pageable);
}
