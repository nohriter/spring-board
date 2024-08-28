package com.nori.springboard.controller;

import com.nori.springboard.config.Login;
import com.nori.springboard.service.CommentService;
import com.nori.springboard.service.PostResponse;
import com.nori.springboard.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@GetMapping("/board/write")
	public String writePostForm(@Login Long memberId, Model model) {
		model.addAttribute("memberId", memberId);
		return "/post/writePost";
	}

	@GetMapping("/board/view/{postId}/update")
	public String editPostForm(@Login Long memberId, @PathVariable Long postId, Model model) {
		log.info("/board/view/{postId}/update");
		PostResponse post = postService.getPost(postId);

		postService.verifyPostOwner(memberId, post.getWriterId());

		model.addAttribute("post", post);
		model.addAttribute("category", post.getCategoryName());

		return "/post/editPost";
	}

	@PostMapping("/board/{postId}/update")
	public String editPost(@Login Long memberId, @PathVariable Long postId, @ModelAttribute PostRequest request,
		RedirectAttributes redirectAttributes) {
		log.info("/board/{postId}/update");

		postService.updatePost(memberId, postId, request);

		redirectAttributes.addAttribute("postId", postId);

		return "redirect:/board/view/{postId}";
	}

	@GetMapping("/board/view")
	public String getPosts(@RequestParam(value = "category", defaultValue = "all") String category,
		@RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model) {
		Page<PostResponse> posts = postService.getPostPages(category, pageNumber);

		int totalPages = posts.getTotalPages();

		model.addAttribute("posts", posts);
		model.addAttribute("category", category);
		model.addAttribute("totalPages", totalPages);

		return "index";
	}

	@GetMapping("/board/view/{postId}")
	public String getPost(@PathVariable Long postId, Model model) {
		log.info("/board/view/{postId}");
		PostResponse post = postService.getPost(postId);

		model.addAttribute("post", post);
		model.addAttribute("category", post.getCategoryName());

		return "/post/viewPost";
	}

	@PostMapping("/board/write")
	public String writePost(@Login Long memberId, @ModelAttribute PostRequest request,  RedirectAttributes redirect) {
		PostResponse response = postService.postCreate(memberId, request);

		redirect.addAttribute("postId", response.getId());

		return "redirect:/board/view/{postId}";
	}

}
