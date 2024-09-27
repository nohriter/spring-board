package com.nori.springboard.controller.exceptionHandler;

import com.nori.springboard.exception.BaseException;
import com.nori.springboard.exception.InvalidFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<Map<String, String>> baseException(BaseException e) {
		Map<String, String> response = new HashMap<>();
		response.put("status", e.getCode());
		response.put("message", e.getMessage());

		log.error("Exception occurred: code: {}, message: {}", e.getCode(), e.getMessage(), e);

		return ResponseEntity.ok().body(response);  // 400 Bad Request 상태 반환
	}

	@ExceptionHandler(InvalidFileException.class)
	public ResponseEntity<Map<String, Object>> invalidFileException(InvalidFileException e) {
		Map<String, Object> response = new HashMap<>();
		Map<String, String> error = new HashMap<>();
		error.put("message", e.getMessage());
		response.put("error", error);

		log.error("Exception occurred: message: {}", e.getMessage(), e);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 400 Bad Request 상태 반환
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException e) {
		Map<String, String> response = new HashMap<>();
		response.put("message", "파일 크기가 너무 큽니다. 최대 1MB까지 업로드 가능합니다.");

		log.error("Exception occurred: message: {}", e.getMessage(), e);

		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);  // 413 상태 반환
	}


}
