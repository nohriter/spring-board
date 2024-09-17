package com.nori.springboard.controller.login;


import com.fasterxml.jackson.core.JsonProcessingException;

import com.nori.springboard.service.MemberService;
import com.nori.springboard.service.email.EmailService;
import com.nori.springboard.service.login.KakaoAccessToken;
import com.nori.springboard.service.login.KakaoMemberInfoResponse;
import com.nori.springboard.service.login.LoginService;
import com.nori.springboard.service.login.LoginMemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;
	private final EmailService emailService;
	private final MemberService memberService;

	@GetMapping("/kakao-login")
	public String login(@RequestParam(name = "code") String code, HttpServletRequest request) throws JsonProcessingException {
		KakaoAccessToken accessToken = loginService.getAccessToken(code);

		KakaoMemberInfoResponse memberInfo = loginService.getMemberInfo(accessToken);

		Long memberId = loginService.findMemberOrJoin(memberInfo);

		loginService.createSession(memberId, request);

		return "redirect:/";
	}

	@PostMapping("/login")
	@ResponseBody
	public ResponseEntity<Map<String, String>> login(@RequestBody EmailLoginRequest loginRequest, HttpServletRequest request) {
		log.info("loginRequest.getLoginId : {}", loginRequest.getLoginId());

		Long memberId = loginService.login(loginRequest);
		loginService.createSession(memberId, request);

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "로그인에 성공했습니다.");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session != null) {
			session.invalidate();
		}

		return "redirect:/";
	}

	@GetMapping("/signup")
	public String join() {
		return "member/signup";
	}

	@PostMapping("/signup")
	public String join(@ModelAttribute SignupRequest request) {
		LoginMemberResponse response = loginService.signUp(request);

		emailService.sendSimpleEmail(response.getLoginId(), response.getToken());

		return "member/sendVerificationLink";
	}

	@GetMapping("/verify-email")
	public String verifyEmail(@RequestParam String token) {
		loginService.verifyEmail(token);

		return "member/emailVerified";
	}

	@GetMapping("/resend-verification")
	public String resendMail() {
		return "member/resend-verification";
	}

	@PostMapping("/resend-verification")
	@ResponseBody
	public ResponseEntity<Map<String, String>> resendVerificationEmail(@RequestBody EmailsendRequest request) {
		LoginMemberResponse loginResponse = loginService.resendMail(request.getLoginId());

		emailService.sendSimpleEmail(loginResponse.getLoginId(), loginResponse.getToken());

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "인증 메일을 다시 전송했습니다.");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/find-password")
	public String resendVerificationEmail() {
		return "member/findPassword";
	}


	@PostMapping("/find-password")
	@ResponseBody
	public ResponseEntity<Map<String, String>> findPassword(@RequestBody EmailsendRequest request) {
		LoginMemberResponse loginMemberResponse = loginService.passwordReset(request.getLoginId());

		emailService.sendEmailPasswordReset(loginMemberResponse.getLoginId(), loginMemberResponse.getToken());

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "인증 메일을 다시 전송했습니다.");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/update-password")
	public String updatePassword(@RequestParam String token, Model model) {
		String tokenValue = loginService.verifyToken(token);

		model.addAttribute("token", tokenValue);

		return "member/update-password";
	}

	@PostMapping("/update-password")
	@ResponseBody
	public ResponseEntity<Map<String, String>> updatePassword(@RequestBody PasswordUpdateRequest request) {
		memberService.passwordUpdate(request);

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "비밀번호 변경을 완료하였습니다.");

		return ResponseEntity.ok(response);
	}


}
