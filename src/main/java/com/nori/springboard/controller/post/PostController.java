package com.nori.springboard.controller.post;

import com.nori.springboard.config.Login;
import com.nori.springboard.entity.category.CategoryRepository;
import com.nori.springboard.entity.category.CategoryType;
import com.nori.springboard.service.category.CategoryResponse;
import com.nori.springboard.service.category.CategoryService;
import com.nori.springboard.service.post.PostResponse;
import com.nori.springboard.service.post.PostService;
import java.beans.PropertyEditorSupport;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
	private final CategoryService categoryService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(CategoryType.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(CategoryType.of(text));  // 대소문자 구분 없이 처리
			}
		});
	}

	@GetMapping("/board/lists")
	public String getPosts(@RequestParam(value = "id", defaultValue = "free") String boardTitle,
		@RequestParam(value = "category", defaultValue = "all") String category,
		@RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model) {

		Page<PostResponse> posts = postService.getPostPages(boardTitle, category, pageNumber);
		List<CategoryResponse> categories = categoryService.getCategoriesByBoard(boardTitle); // 게시판에 따른 카테고리 가져오기

		int totalPages = posts.getTotalPages();

		model.addAttribute("posts", posts);
		model.addAttribute("category", category);
		model.addAttribute("boardTitle", boardTitle);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("categories", categories);

		return "index";
	}

	@GetMapping("/board/view/{postId}")
	public String getPost(@RequestParam(value = "id") String boardTitle, @PathVariable Long postId, Model model) {
		PostResponse post = postService.getPost(postId);

		model.addAttribute("boardTitle", boardTitle);
		model.addAttribute("post", post);
		model.addAttribute("category", post.getCategoryName());

		return "post/viewPost";
	}

	@GetMapping("/board/write")
	public String writePostForm(@Login Long memberId, @RequestParam(value = "id") String boardTitle, Model model) {
		List<CategoryResponse> categories = categoryService.getCategoriesByBoard(boardTitle);

		model.addAttribute("memberId", memberId);
		model.addAttribute("categories", categories);
		model.addAttribute("boardTitle", boardTitle);

		return "post/writePost";
	}

	@PostMapping("/board/write")
	public String writePost(@Login Long memberId, @ModelAttribute PostRequest request,  RedirectAttributes redirect) {
		PostResponse response = postService.postCreate(memberId, request);

		redirect.addAttribute("postId", response.getId());

		return "redirect:/board/view/{postId}";
	}

	@GetMapping("/board/modify/{postId}")
	public String editPostForm(@Login Long memberId, @RequestParam(value = "id") String boardTitle,
		@PathVariable Long postId, Model model) {
		PostResponse post = postService.getPost(postId);

		postService.verifyPostOwner(memberId, post.getWriterId());
		List<CategoryResponse> categories = categoryService.getCategoriesByBoard(boardTitle);

		model.addAttribute("boardTitle", boardTitle);
		model.addAttribute("post", post);
		model.addAttribute("categories", categories);

		return "post/editPost";
	}

	@PostMapping("/board/{postId}/modify")
	public String editPost(@Login Long memberId, @PathVariable Long postId, @ModelAttribute PostRequest request,
		RedirectAttributes redirectAttributes) {

		postService.updatePost(memberId, postId, request);

		redirectAttributes.addAttribute("postId", postId);
		redirectAttributes.addAttribute("boardTitle", request.getBoardTitle());

		return "redirect:/board/view/{postId}?id={boardTitle}";
	}

	@PostMapping("/board/{postId}/delete")
	public String deletePost(@Login Long memberId, @PathVariable Long postId) {
		postService.deletePost(memberId, postId);

		return "redirect:/";
	}

}
