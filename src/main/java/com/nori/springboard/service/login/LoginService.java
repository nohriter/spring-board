package com.nori.springboard.service.login;

import static com.nori.springboard.config.SessionConst.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nori.springboard.controller.login.EmailLoginRequest;
import com.nori.springboard.controller.login.SignupRequest;
import com.nori.springboard.entity.login.EmailVerificationTokenRepository;
import com.nori.springboard.entity.member.Member;
import com.nori.springboard.entity.member.MemberRepository;
import com.nori.springboard.exception.AlreadyVerifyEmailException;
import com.nori.springboard.exception.ExpiredTokenException;
import com.nori.springboard.exception.InvalidParameterException;
import com.nori.springboard.exception.InvalidLoginInfoException;
import com.nori.springboard.exception.NotVerifyEmailException;
import com.nori.springboard.entity.login.EmailVerificationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

	private static final String REDIRECT_URI  = "http://www.jhub.kr/kakao-login";
	private static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";
	private static final String USER_INFO_REQUEST_URI = "https://kapi.kakao.com/v2/user/me";
	private static final String KAKAO = "KAKAO_";

	private final RestTemplate restTemplate;
	private final MemberRepository memberRepository;
	private final EmailVerificationTokenRepository tokenRepository;

	@Value("${KAKAO_CLIENT_ID}")
	private String clientId;

	/**
	 * 카카오 토큰 요청
	 */
	public KakaoAccessToken getAccessToken(String code) {
		MultiValueMap<String, String> requestBody = getRequestBody(code);
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

	/**
	 * 카카오 회원 정보 가져오기
	 */
	public KakaoMemberInfoResponse getMemberInfo(KakaoAccessToken accessToken) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", accessToken.getTokenHeaderString());
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> request = new HttpEntity<>(headers);

		String memberInfo = restTemplate.exchange(USER_INFO_REQUEST_URI, HttpMethod.GET, request, String.class).getBody();
		return new ObjectMapper().readValue(memberInfo, KakaoMemberInfoResponse.class);
	}

	/**
	 * 카카오 로그인 및 회원가입 처리
	 */
	public Long findMemberOrJoin(KakaoMemberInfoResponse memberInfo) {
		return memberRepository.findByLoginId(KAKAO + memberInfo.getEmail())
			.orElseGet(() -> memberRepository.save(memberInfo.toEntity()))
			.getId();
	}

	/**
	 * 세션 생성
	 */
	public void createSession(Long memberId, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(LOGIN_MEMBER, memberId);
	}

	/**
	 * 이메일 회원가입
	 */
	public LoginMemberResponse signUp(SignupRequest request) {
		verifyEmailAndPassword(request.getLoginId(), request.getPassword(), request.getPasswordConfirm());

		memberRepository.findByLoginId(request.getLoginId()).ifPresent(member -> {
			throw new AlreadyVerifyEmailException();
		});

		String encryptedPassword = PasswordManager.encryptPassword(request.getPassword());
		Member member = request.toEntity(encryptedPassword);
		memberRepository.save(member);

		EmailVerificationToken token = EmailVerificationToken.of(member);
		tokenRepository.save(token);

		return LoginMemberResponse.of(member, token.getToken());
	}

	private void verifyEmailAndPassword(String email, String password, String passwordConfirm) {
		if (!IdPasswordValidator.isValidEmail(email)) {
			throw new InvalidLoginInfoException();
		}
		if (!IdPasswordValidator.isValidPassword(password, passwordConfirm)) {
			throw new InvalidLoginInfoException();
		}
	}

	/**
	 * 이메일 인증 처리
	 */
	public void verifyEmail(String token) {
		EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
			.orElseThrow(InvalidParameterException::new);
		if (verificationToken.isExpired()) {
			throw new ExpiredTokenException();
		}

		Member member = verificationToken.getMember();
		member.emailVerify();
		memberRepository.save(member);
		tokenRepository.delete(verificationToken);
	}

	/**
	 * 이메일 로그인
	 */
	public Long login(EmailLoginRequest request) {
		Member member = findMemberByLoginId(request.getLoginId());
		validatePassword(request.getPassword(), member.getPassword());
		validateEmailVerification(member);

		return member.getId();
	}

	/**
	 * 인증 메일 재전송
	 */
	public LoginMemberResponse resendMail(String loginId) {
		Member member = findMemberByLoginId(loginId);
		if (member.isEmailVerified()) {
			throw new AlreadyVerifyEmailException();
		}

		EmailVerificationToken token = tokenRepository.findByMemberId(member.getId())
			.orElseGet(() -> EmailVerificationToken.of(member));
		token.updateExpiration();
		tokenRepository.save(token);

		return LoginMemberResponse.of(member, token.getToken());
	}

	public LoginMemberResponse passwordReset(String loginId) {
		Member member = findMemberByLoginId(loginId);

		EmailVerificationToken token = EmailVerificationToken.of(member);
		tokenRepository.save(token);

		return LoginMemberResponse.of(member, token.getToken());
	}

	public String verifyToken(String token) {
		EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
			.orElseThrow(InvalidParameterException::new);

		if (verificationToken.isExpired()) {
			throw new ExpiredTokenException();
		}

		return verificationToken.getToken();
	}

	private Member findMemberByLoginId(String loginId) {
		return memberRepository.findByLoginId(loginId)
			.orElseThrow(InvalidLoginInfoException::new);
	}

	private void validatePassword(String rawPassword, String encryptedPassword) {
		if (!PasswordManager.isPasswordMatch(rawPassword, encryptedPassword)) {
			throw new InvalidLoginInfoException();
		}
	}

	private void validateEmailVerification(Member member) {
		if (!member.isEmailVerified()) {
			throw new NotVerifyEmailException();
		}
	}
}
