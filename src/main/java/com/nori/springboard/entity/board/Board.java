package com.nori.springboard.entity.board;


import com.nori.springboard.entity.category.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

	@Id
	@Column(name = "board_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String title;

	@OneToMany(mappedBy = "board") private Collection<Category> category;

	public Collection<Category> getCategory() {
		return category;
	}

	public void setCategory(Collection<Category> category) {
		this.category = category;
	}
}
