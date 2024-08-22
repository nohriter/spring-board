package com.nori.springboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nori.springboard.entity.Member;
import com.nori.springboard.service.KakaoAccessToken;
import com.nori.springboard.service.KakaoMemberInfoResponse;
import com.nori.springboard.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;

	@GetMapping("/login")
	public String login(@RequestParam(name = "code") String code, HttpServletRequest request) throws JsonProcessingException {
		KakaoAccessToken accessToken = loginService.getAccessToken(code);

		KakaoMemberInfoResponse memberInfo = loginService.getMemberInfo(accessToken);

		Member member = loginService.findMemberOrJoin(memberInfo);

		loginService.createSession(member.getId(), request);

		return "redirect:/";
	}

}
