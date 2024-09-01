package com.nori.springboard.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity  {

	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 500)
	private String content;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member writer;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Comment parent;

	@OneToMany(mappedBy = "parent")
	private List<Comment> replies = new ArrayList<>();

	private boolean isReply;

	private boolean isDeleted;

	private String parentNickname;

	@Builder
	private Comment(String content, Post post, Member writer, Comment parent,
		String parentNickname, boolean isReply, List<Comment> replies) {
		this.content = content;
		this.post = post;
		this.writer = writer;
		this.parent = parent;
		this.parentNickname = parentNickname;
		this.isReply = isReply;
		this.replies = replies;
	}

	public void delete() {
		isDeleted = true;
	}
}
