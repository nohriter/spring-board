package com.nori.springboard.service.category;

import com.nori.springboard.entity.category.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public List<CategoryResponse> getCategoriesByBoard(String boardTitle) {
		return categoryRepository.findByBoard_Title(boardTitle).stream()
			.map(category -> CategoryResponse.of(category))
			.toList();
	}

}
