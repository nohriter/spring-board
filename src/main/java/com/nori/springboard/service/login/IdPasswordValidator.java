package com.nori.springboard.service.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdPasswordValidator {

	public static boolean isValidEmail(String email) {
		// 이메일 패턴 정의
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		Pattern pattern = Pattern.compile(emailRegex);
		if (email == null || email.isEmpty()) {
			return false; // 이메일이 비어있을 경우 false
		}
		Matcher matcher = pattern.matcher(email);
		return matcher.matches(); // 이메일 패턴에 맞으면 true, 아니면 false
	}

	// 비밀번호 검증 로직
	public static boolean isValidPassword(String password, String passwordConfirm) {
		if (password == null || password.isEmpty()) {
			return false; // 비밀번호가 비어있으면 false 반환
		}
		if (!password.equals(passwordConfirm)) {
			return false; // 비밀번호가 비어있으면 false 반환
		}

		// 조건 1: 영문, 숫자, 특수문자 중 2가지 이상 포함
		boolean hasLetter = password.matches(".*[A-Za-z].*");
		boolean hasNumber = password.matches(".*\\d.*");
		boolean hasSpecialChar = password.matches(".*[^\\w\\s].*");

		int typeCount = 0;
		if (hasLetter) typeCount++;
		if (hasNumber) typeCount++;
		if (hasSpecialChar) typeCount++;

		// 2가지 이상의 조합이어야 함
		if (typeCount < 2) {
			return false;
		}

		// 조건 2: 8자 이상 32자 이하 (공백 제외)
		if (password.length() < 8 || password.length() > 32 || password.contains(" ")) {
			return false;
		}

		// 조건 3: 연속 3자 이상 동일한 문자 제외
		if (hasConsecutiveCharacters(password)) {
			return false;
		}

		// 모든 조건 통과 시 true 반환
		return true;
	}

	// 연속 3자 이상 동일한 문자가 있는지 확인
	private static boolean hasConsecutiveCharacters(String password) {
		for (int i = 0; i < password.length() - 2; i++) {
			char current = password.charAt(i);
			if (current == password.charAt(i + 1) && current == password.charAt(i + 2)) {
				return true; // 연속된 3자가 동일하면 true 반환
			}
		}
		return false;
	}
}
