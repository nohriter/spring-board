package com.nori.springboard.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {

	public static String toTodayOrNot(LocalDateTime dateTime) {
		LocalDateTime now = LocalDateTime.now();

		// 오늘 날짜와 비교
		if (dateTime.toLocalDate().isEqual(now.toLocalDate())) {
			// 당일이면 24h:mm 형식으로 출력
			DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
			return dateTime.format(time);
		} else {
			// 당일이 아니면 yyyy-MM-dd 형식으로 출력
			DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			return dateTime.format(time);
		}
	}
	public static String toDateTime(LocalDateTime dateTime) {
		// 당일이 아니면 yyyy-MM-dd 형식으로 출력
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
		return dateTime.format(dateTimeFormat);
	}
}
