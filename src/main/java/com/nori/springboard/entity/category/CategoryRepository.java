package com.nori.springboard.entity.category;

import com.nori.springboard.entity.board.Board;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByCategoryType(CategoryType categoryType);

	List<Category> findByBoard_Title(String boardTitle);

	Optional<Category> findByCategoryTypeAndBoard(CategoryType categoryType, Board board);

}
