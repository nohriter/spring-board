package com.nori.springboard.service.comment;

import com.nori.springboard.controller.comment.CommentEditRequest;
import com.nori.springboard.controller.comment.CommentRequest;
import com.nori.springboard.entity.comment.Comment;
import com.nori.springboard.entity.comment.CommentRepository;
import com.nori.springboard.entity.member.Member;
import com.nori.springboard.entity.member.MemberRepository;
import com.nori.springboard.entity.post.Post;
import com.nori.springboard.entity.post.PostRepository;
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

	@Transactional
	public void editComment(Long memberId, CommentEditRequest request) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NoSuchElementException());

		Post post = postRepository.findById(request.getPostId())
			.orElseThrow(() -> new NoSuchElementException());

		Comment comment = commentRepository.findById(request.getCommentId())
			.orElseThrow(() -> new NoSuchElementException());

		verifyCommentOwner(memberId, comment.getWriter().getId());
		verifyContent(request.getContent());

		comment.editContent(request.getContent());
	}

	private void verifyCommentOwner(Long memberId, Long writerId) {
		if (!memberId.equals(writerId)) {
			log.error("memberId: {}, writerId: {}", memberId, writerId);
			throw new IllegalStateException("작성자가 다릅니다.");
		}
	}

	private void verifyContent(String content) {
		if(content.length() > 1000) {
			throw new IllegalArgumentException("댓글 길이 허용치 초과");
		}
	}
}
