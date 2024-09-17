package com.nori.springboard.entity.member;


import com.nori.springboard.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
	
	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String loginId;

	@Column(nullable = false, length = 10)
	private String nickname;

	@Column(nullable = true, length = 60)
	private String password;

	private boolean isDeleted = false;

	private boolean isEmailVerified = false;

	@Builder
	public Member(String loginId, String nickname, String password) {
		this.loginId = loginId;
		this.nickname = nickname;
		this.password = password;
	}

	public void emailVerify() {
		isEmailVerified = true;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

}
