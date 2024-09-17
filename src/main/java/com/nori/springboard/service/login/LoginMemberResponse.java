package com.nori.springboard.service.login;

import com.nori.springboard.entity.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginMemberResponse {

	private Long memberId;
	private String loginId;
	private String token;

	@Builder
	private LoginMemberResponse(Long memberId, String loginId, String token) {
		this.memberId = memberId;
		this.loginId = loginId;
		this.token = token;
	}

	public static LoginMemberResponse of(Member member, String token) {
		return LoginMemberResponse.builder()
			.memberId(member.getId())
			.loginId(member.getLoginId())
			.token(token)
			.build();
	}
}
