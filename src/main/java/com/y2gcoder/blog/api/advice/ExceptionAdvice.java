package com.y2gcoder.blog.api.advice;

import com.y2gcoder.blog.service.auth.SignInFailureException;
import com.y2gcoder.blog.api.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	public ApiResponse exception(Exception e) {
		log.error("Unknown Exception!!", e);
		return ApiResponse.failure(-9999, "예상치 못한 에러가 발생했습니다.");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(BAD_REQUEST)
	public ApiResponse illegalArgumentException(IllegalArgumentException e) {
		log.info("비즈니스 로직 에러: {}", e.getMessage());
		return ApiResponse.failure(-1000, e.getMessage());
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(BAD_REQUEST)
	public ApiResponse bindException(BindException e) {
		log.info("유효성 검증 에러 발생", e);
		return ApiResponse.failure(-1001, e);
	}

	@ExceptionHandler(SignInFailureException.class)
	@ResponseStatus(UNAUTHORIZED)
	public ApiResponse signInFailureException(SignInFailureException e) {
		log.info("로그인 예외 : {}", e.getMessage());
		return ApiResponse.failure(-1002, "로그인 예외 : " + e.getMessage());
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(FORBIDDEN)
	public ApiResponse accessDeniedException() {
		return ApiResponse.failure(-1003, "접근이 거부되었습니다.");
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	@ResponseStatus(BAD_REQUEST)
	public ApiResponse missingRequestHeaderException(MissingRequestHeaderException e) {
		return ApiResponse.failure(-1004, e.getHeaderName() + " 요청 헤더가 누락되었습니다.");
	}

}
