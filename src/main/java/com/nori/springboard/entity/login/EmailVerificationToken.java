package com.nori.springboard.entity.login;

import com.nori.springboard.entity.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "email_verification_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationToken {

	@Id
	@Column(name = "email_verification_token_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "member_id")
	private Member member;  // 회원과 연결

	private String token;
	private LocalDateTime expiryDate;  // 만료 시간

	@Builder
	private EmailVerificationToken(Member member, String token) {
		this.member = member;
		this.token = token;
		this.expiryDate = LocalDateTime.now().plusHours(24);  // 24시간 유효
	}

	public static EmailVerificationToken of(Member member) {
		return EmailVerificationToken.builder()
			.member(member)
			.token(UUID.randomUUID().toString())
			.build();
	}

	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expiryDate);
	}

	public void updateExpiration() {
		this.expiryDate = LocalDateTime.now().plusDays(1);
	}

}

