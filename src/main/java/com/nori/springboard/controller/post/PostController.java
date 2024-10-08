package com.nori.springboard.controller.post;

import com.nori.springboard.config.Login;
import com.nori.springboard.entity.category.CategoryType;
import com.nori.springboard.service.category.CategoryResponse;
import com.nori.springboard.service.category.CategoryService;
import com.nori.springboard.service.image.ImageService;
import com.nori.springboard.service.post.PostDetailResponse;
import com.nori.springboard.service.post.PostGuestService;
import com.nori.springboard.service.post.PostResponse;
import com.nori.springboard.service.post.PostService;
import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;
	private final PostGuestService postGuestService;
	private final CategoryService categoryService;
	private final ImageService imageService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(CategoryType.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(CategoryType.of(text));
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
	public String getPost(@RequestParam(value = "id") String boardTitle,
		@RequestParam(value = "category") String category, @PathVariable Long postId, Model model) {

		PostDetailResponse response = postService.getPost(postId);

		model.addAttribute("boardTitle", boardTitle);
		model.addAttribute("post", response);
		model.addAttribute("category", category);
		model.addAttribute("categoryName", response.getCategoryName());

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
		redirect.addAttribute("boardTitle", request.getBoardTitle());
		redirect.addAttribute("category", response.getCategoryEngName());

		return "redirect:/board/view/{postId}?id={boardTitle}&category={category}";
	}

	@GetMapping("/board/modify/{postId}")
	public String modifyPostForm(@Login Long memberId, @RequestParam(value = "id") String boardTitle,
		@PathVariable Long postId, Model model) {
		PostDetailResponse response = postService.getPost(postId);

		postService.verifyPostOwner(memberId, response.getWriterId());
		List<CategoryResponse> categories = categoryService.getCategoriesByBoard(boardTitle);

		model.addAttribute("boardTitle", boardTitle);
		model.addAttribute("post", response);
		model.addAttribute("categories", categories);

		return "post/editPost";
	}

	@PostMapping("/board/{postId}/modify")
	public String editPost(@Login Long memberId, @PathVariable Long postId, @ModelAttribute PostRequest request,
		RedirectAttributes redirect) {

		PostResponse response = postService.updatePost(memberId, postId, request);

		redirect.addAttribute("postId", postId);
		redirect.addAttribute("boardTitle", request.getBoardTitle());
		redirect.addAttribute("category", response.getCategoryEngName());

		return "redirect:/board/view/{postId}?id={boardTitle}&category={category}";
	}

	@PostMapping("/board/{postId}/delete")
	public String deletePost(@Login Long memberId, @PathVariable Long postId) {
		postService.deletePost(memberId, postId);

		return "redirect:/";
	}

	@GetMapping("/board/guest/write")
	public String writePostForm(@RequestParam(value = "id") String boardTitle, Model model) {
		List<CategoryResponse> categories = categoryService.getCategoriesByBoard(boardTitle);

		model.addAttribute("categories", categories);
		model.addAttribute("boardTitle", boardTitle);

		return "post/guest/guestWritePost";
	}

	@PostMapping("/board/guest/write")
	public String writePost(@ModelAttribute PostGuestRequest request,  RedirectAttributes redirect) {
		PostResponse response = postGuestService.postCreate(request);

		redirect.addAttribute("postId", response.getId());
		redirect.addAttribute("boardTitle", request.getBoardTitle());
		redirect.addAttribute("category", response.getCategoryEngName());

		return "redirect:/board/view/{postId}?id={boardTitle}&category={category}";
	}

	@GetMapping("/board/guest/delete/{postId}")
	public String deleteGuestPostForm(@PathVariable Long postId, @RequestParam(value = "id") String boardTitle,
		@RequestParam(value = "category") String category, Model model) {

		postGuestService.validatePostExists(postId);

		model.addAttribute("postId", postId);
		model.addAttribute("category", category);
		model.addAttribute("boardTitle", boardTitle);

		return "post/guest/guestDeletePost";
	}

	@PostMapping("/board/guest/{postId}/delete")
	@ResponseBody
	public ResponseEntity<Map<String, String>> deleteGuestPost(@PathVariable Long postId, @RequestBody PostPasswordRequest request) {
		postGuestService.deletePost(postId, request.getPassword());

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "게시글을 삭제하였습니다.");

		return ResponseEntity.ok(response);
	}

}
