package com.nori.springboard.entity.login;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

	Optional<EmailVerificationToken> findByToken(String token);
	Optional<EmailVerificationToken> findByMemberId(Long memberId);

}
