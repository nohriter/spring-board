package com.nori.springboard.service;

import com.nori.springboard.controller.login.PasswordUpdateRequest;
import com.nori.springboard.entity.login.EmailVerificationTokenRepository;
import com.nori.springboard.entity.member.Member;
import com.nori.springboard.entity.member.MemberRepository;
import com.nori.springboard.exception.ExpiredTokenException;
import com.nori.springboard.exception.InvalidEmailOrPasswordException;
import com.nori.springboard.exception.InvalidParameterException;
import com.nori.springboard.entity.login.EmailVerificationToken;
import com.nori.springboard.service.login.IdPasswordValidator;
import com.nori.springboard.service.login.PasswordManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final EmailVerificationTokenRepository tokenRepository;

	@Transactional
	public void passwordUpdate(PasswordUpdateRequest request) {
		EmailVerificationToken verificationToken = tokenRepository.findByToken(request.getToken())
			.orElseThrow(InvalidParameterException::new);

		if (verificationToken.isExpired()) {
			throw new ExpiredTokenException();
		}

		verifyPassword(request.getPassword(), request.getPasswordConfirm());

		Member member = verificationToken.getMember();

		String encryptedPassword = PasswordManager.encryptPassword(request.getPassword());

		member.updatePassword(encryptedPassword);

		tokenRepository.delete(verificationToken);
	}

	private void verifyPassword(String password, String passwordConfirm) {
		if (!IdPasswordValidator.isValidPassword(password, passwordConfirm)) {
			throw new InvalidEmailOrPasswordException();
		}
	}

}
