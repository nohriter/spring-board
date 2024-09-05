package com.nori.springboard.controller;

import com.nori.springboard.config.Login;
import com.nori.springboard.service.CommentResponse;
import com.nori.springboard.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/comments/add")
	public CommentResponse createComment(@Login Long memberId , @RequestBody CommentRequest commentRequest) {
		CommentResponse response = commentService.createComment(memberId, commentRequest);

		return response;
	}

	@PostMapping("/comments")
	public List<CommentResponse> getComments(@RequestBody CommentsReadRequest request) {
		List<CommentResponse> comments = commentService.getComments(request.getPostId());

		return comments;
	}

	@PostMapping("/comments/{commentId}/edit")
	public ResponseEntity editComment(@Login Long memberId, @PathVariable Long commentId,
		@ModelAttribute CommentEditRequest request) {
		commentService.editComment(memberId, request);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/comments/{commentId}/delete")
	public ResponseEntity deleteComment(@Login Long memberId, @PathVariable Long commentId) {
		commentService.deleteComment(memberId, commentId);

		return ResponseEntity.ok().build();
	}
}
