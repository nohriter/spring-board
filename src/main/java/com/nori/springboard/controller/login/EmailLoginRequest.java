package com.nori.springboard.controller.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmailLoginRequest {

	private String loginId;
	private String password;

}
