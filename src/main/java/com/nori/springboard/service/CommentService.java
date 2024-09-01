package com.nori.springboard.service;

import com.nori.springboard.controller.CommentRequest;
import com.nori.springboard.entity.Comment;
import com.nori.springboard.entity.CommentRepository;
import com.nori.springboard.entity.Member;
import com.nori.springboard.entity.MemberRepository;
import com.nori.springboard.entity.Post;
import com.nori.springboard.entity.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;

	@Transactional
	public List<CommentResponse> getComments(Long postId) {
		List<Comment> comments = commentRepository.findCommentsWithRepliesByPostId(postId);

		return comments.stream()
			.map(comment -> CommentResponse.of(comment, comment.getReplies().stream()
				.map(CommentResponse::of)
				.toList()))
			.toList();
	}

	@Transactional
	public CommentResponse createComment(Long memberId, CommentRequest commentRequest) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NoSuchElementException());

		Post post = postRepository.findById(commentRequest.getPostId())
			.orElseThrow(() -> new NoSuchElementException());

		Comment parentsComment = findParentsCommentById(commentRequest.getParentCommentId());

		Comment comment = toEntity(commentRequest, parentsComment, member, post);

		return CommentResponse.of(commentRepository.save(comment));
	}

	private Comment toEntity(CommentRequest commentRequest, Comment parentsComment, Member member, Post post) {
		if(parentsComment == null) { // 최초의 댓글
			return commentRequest.toEntity(member, post, null);
		} else if(parentsComment.isReply()) { // 답글의 답글
			Comment grandParentsComment = findParentsCommentById(parentsComment.getParent().getId());
			return commentRequest.toEntity(member, post, grandParentsComment, parentsComment.getWriter().getNickname());
		}else { // 댓글의 답글
			return commentRequest.toEntity(member, post, parentsComment, parentsComment.getWriter().getNickname());
		}
	}

	@Transactional
	public void deleteComment(Long memberId, Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.filter(c -> c.getWriter().getId().equals(memberId))
			.orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없거나 작성자가 다릅니다."));

		comment.delete();
	}

	private Comment findParentsCommentById(Long commentId) {
		return commentId == null ? null : commentRepository.findById(commentId)
			.orElseThrow(() -> new NoSuchElementException("부모 댓글을 찾을 수 없습니다."));
	}
}
