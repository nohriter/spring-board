package com.nori.springboard.controller.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PasswordUpdateRequest {

	private String token;
	private String password;
	private String passwordConfirm;

}
