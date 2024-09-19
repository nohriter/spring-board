package com.nori.springboard.entity.post;


import com.nori.springboard.entity.BaseEntity;
import com.nori.springboard.entity.board.Board;
import com.nori.springboard.entity.category.Category;
import com.nori.springboard.entity.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String title;

	@Column(nullable = false, length = 2000)
	private String content;

	private long viewCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member writer;

	private boolean isDeleted;

	@Builder
	private Post(String title, String content, Board board, Category category,
		Member writer) {
		this.title = title;
		this.content = content;
		this.board = board;
		this.category = category;
		this.writer = writer;
	}

	public void update(Category category, String title, String content) {
		this.category = category;
		this.title = title;
		this.content = content;
	}
}
