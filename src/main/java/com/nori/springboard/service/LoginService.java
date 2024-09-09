package com.nori.springboard.service;

import static com.nori.springboard.config.SessionConst.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nori.springboard.entity.Member;
import com.nori.springboard.entity.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LoginService {

	private static final String REDIRECT_URI 		  = "https://www.jhub.kr/login";
	private static final String TOKEN_REQUEST_URI 	  = "https://kauth.kakao.com/oauth/token";
	private static final String USER_INFO_REQUEST_URI = "https://kapi.kakao.com/v2/user/me";

	private final RestTemplate restTemplate;
	private final MemberRepository memberRepository;

	@Value("${KAKAO_CLIENT_ID}")
	private String clientId;

	public KakaoAccessToken getAccessToken(String code) {
		MultiValueMap<String, String> requestBody =  getRequestBody(code);

		return restTemplate.postForObject(TOKEN_REQUEST_URI, requestBody, KakaoAccessToken.class);
	}

	private MultiValueMap<String, String> getRequestBody(String code) {
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("grant_type", "authorization_code");
		requestBody.add("client_id", clientId);
		requestBody.add("redirect_uri", REDIRECT_URI);
		requestBody.add("code", code);

		return requestBody;
	}

	public KakaoMemberInfoResponse getMemberInfo(KakaoAccessToken accessToken) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", accessToken.getTokenHeaderString());
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<String> request = new HttpEntity<>(headers);

		String memberInfo = restTemplate.exchange(USER_INFO_REQUEST_URI, HttpMethod.GET, request, String.class).getBody();

		return new ObjectMapper().readValue(memberInfo, KakaoMemberInfoResponse.class);
	}

	public Member findMemberOrJoin(KakaoMemberInfoResponse memberInfo) {
		return memberRepository.findByLoginId(memberInfo.getEmail())
			.orElseGet(() -> memberRepository.save(memberInfo.toEntity()));
	}

	public void createSession(Long id, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(LOGIN_MEMBER, id);
	}

}
