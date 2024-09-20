package com.nori.springboard.service.login;

import com.nori.springboard.exception.InvalidEmailOrPasswordException;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {

	// 비밀번호 해시화
	public static String encryptPassword(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}

	// 비밀번호 검증
	public static void isPasswordMatch(String plainPassword, String hashedPassword) {
		if(!BCrypt.checkpw(plainPassword, hashedPassword)) {
			throw new InvalidEmailOrPasswordException();
		}
	}
}
