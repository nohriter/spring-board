package com.nori.springboard.controller.exceptionHandler;

import com.nori.springboard.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<Map<String, String>> handleIllegalStateException(BaseException e) {
		Map<String, String> response = new HashMap<>();
		response.put("status", e.getCode());
		response.put("message", e.getMessage());

		// 스택 트레이스 포함 로그
		log.error("Exception occurred: code: {}, message: {}", e.getCode(), e.getMessage(), e);

		return ResponseEntity.ok(response);
	}


}
