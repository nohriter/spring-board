package com.nori.springboard.exception;


import lombok.Getter;

@Getter
public enum ExceptionMessage {
	NOT_FOUND_MEMBER("notFoundMember", "존재하지 않는 유저입니다"),

	NOT_FOUND_POST("notFoundPost", "존재하지 않는 게시글입니다"),

	INVALID_EMAIL_OR_PASSWORD("invalidEmailOrPassword", "이메일 또는 비밀번호를 확인해주세요"),

	NOT_VERIFY_EMAIL("notVerifyEmail", "이메일 인증이 완료되지 않았습니다"),

	ALREADY_VERIFY_EMAIL("alreadyVerifyEmail", "이미 이메일 인증이 완료된 회원입니다"),

	EXPIRED_TOKEN("expiredToken", "이미 만료된 토큰입니다;"),

	INVALID_PARAMETER("invalidParameter", "잘못된 요청입니다."),

	INVALID_FILE("invalidFile", "파일 형식이 올바르지 않습니다.");

	private final String code;
	private final String message;

	ExceptionMessage(String code, String message) {
		this.code = code;
		this.message = message;
	}

}
