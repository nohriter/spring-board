package com.nori.springboard.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
	private final JavaMailSender mailSender;

	@Value("${app.domain}")
	private String domain;  // 환경별로 설정된 도메인을 주입

	@Async
	public void sendSimpleEmail(String toEmail, String token) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(toEmail);
			helper.setSubject("[게시판] 회원가입을 위해 메일을 인증해 주세요.");

			String htmlContent =
				"<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; text-align: center; border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9;'>"
					+ "<h1 style='color: #2196F3;'>이메일 인증</h1>"
					+ "<p style='font-size: 16px; color: #333;'>안녕하세요! 회원가입을 완료하려면 아래 버튼을 클릭해 주세요.</p>"
					+ "<a href='" + domain + "/verify-email?token=" + token + "' "
					+ "style='display: inline-block; padding: 12px 24px; margin-top: 20px; background-color: #2196F3; color: white; text-decoration: none; border-radius: 5px;'>"
					+ "이메일 인증하기</a>"
					+ "<p style='margin-top: 30px; font-size: 14px; color: #999;'>"
					+ "링크는 24시간 동안 유효합니다. 만료된 경우 다시 요청해 주세요."
					+ "</p>"
					+ "</div>";

			helper.setText(htmlContent, true);  // true to enable HTML
			helper.setFrom("nohriter@gmail.com");

			mailSender.send(message);
			log.info("인증 메일이 {}로 전송되었습니다.", toEmail);

		} catch (MessagingException | MailException e) {
			log.error("메일 전송 중 오류 발생: {}", e.getMessage());
		}
	}

	@Async
	public void sendEmailPasswordReset(String toEmail, String token) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(toEmail);
			helper.setSubject("[게시판] 비밀번호 변경 링크입니다.");

			String htmlContent =
				"<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; text-align: center; border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9;'>"
					+ "<h1 style='color: #2196F3;'>비밀번호 변경 링크입니다.</h1>"
					+ "<p style='font-size: 16px; color: #333;'>비밀번호 변경하기 버튼을 눌러 비밀번호를 변경해주세요.</p>"
					+ "<a href='" + domain + "/update-password?token=" + token + "' "
					+ "style='display: inline-block; padding: 12px 24px; margin-top: 20px; background-color: #2196F3; color: white; text-decoration: none; border-radius: 5px;'>"
					+ "비밀번호 변경하기</a>"
					+ "<p style='margin-top: 30px; font-size: 14px; color: #999;'>"
					+ "링크는 24시간 동안 유효합니다. 만료된 경우 다시 요청해 주세요."
					+ "</p>"
					+ "</div>";

			helper.setText(htmlContent, true);  // true to enable HTML
			helper.setFrom("nohriter@gmail.com");

			mailSender.send(message);
			log.info("인증 메일이 {}로 전송되었습니다.", toEmail);

		} catch (MessagingException | MailException e) {
			log.error("메일 전송 중 오류 발생: {}", e.getMessage());
			mailSender.send(message);
		}
	}
}
