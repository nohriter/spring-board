package com.nori.springboard.entity.post;

import com.nori.springboard.entity.category.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("SELECT p FROM Post p WHERE p.board.title = :boardTitle")
	Page<Post> findByBoardTitle(
		@Param("boardTitle") String boardTitle,
		Pageable pageable
	);

	@Query("SELECT p FROM Post p WHERE p.board.title = :boardTitle AND p.category.categoryType = :categoryType")
	Page<Post> findByBoardTitleAndCategoryType(
		@Param("boardTitle") String boardTitle,
		@Param("categoryType") CategoryType categoryType,
		Pageable pageable
	);

}
