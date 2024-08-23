package com.nori.springboard.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nori.springboard.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoMemberInfoResponse {

	private Long id;

	private Properties properties;

	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Properties {

		private String nickname;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoAccount {

		private String email;
	}

	public Member toEntity() {
		return Member.builder()
			.loginId(this.kakaoAccount.email)
			.nickname(this.properties.nickname)
			.build();
	}

	public String getEmail() {
		return kakaoAccount.getEmail();
	}

}
