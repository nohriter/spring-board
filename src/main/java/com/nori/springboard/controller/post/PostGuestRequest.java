package com.nori.springboard.controller.post;

import static com.nori.springboard.service.login.PasswordManager.encryptPassword;

import com.nori.springboard.entity.board.Board;
import com.nori.springboard.entity.category.Category;
import com.nori.springboard.entity.category.CategoryType;
import com.nori.springboard.entity.post.Post;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostGuestRequest {

	private CategoryType categoryType;
	private String category;
	private String boardTitle;
	private String title;
	private String content;
	private String guestNickname;
	private String guestPassword;
	private List<String> imageIds;

	public Post toEntity(Category category, Board board) {
		return Post.builder()
			.category(category)
			.board(board)
			.title(title)
			.content(content)
			.guestNickname(guestNickname)
			.guestPassword(encryptPassword(guestPassword))
			.build();
	}
}
