package com.nori.springboard.controller;

import com.nori.springboard.config.Login;
import com.nori.springboard.service.PostResponse;
import com.nori.springboard.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

	@PostMapping("/board/write")
	public String writePostForm(@Login Long memberId, @ModelAttribute PostWriteRequest request,  RedirectAttributes redirect) {
		PostResponse response = postService.postCreate(memberId, request);

		redirect.addAttribute("postId", response.getId());

		return "redirect:/posts/{postId}";
	}

}
