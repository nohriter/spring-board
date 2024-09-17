package com.nori.springboard.controller.login;

import com.nori.springboard.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SignupRequest {

	private String loginId;
	private String password;
	private String passwordConfirm;

	public Member toEntity(String encryptPassword) {
		return Member.builder()
			.loginId(loginId.toLowerCase())
			.nickname(loginId.split("@")[0])
			.password(encryptPassword)
			.build();
	}
}
