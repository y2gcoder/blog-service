package com.y2gcoder.blog.service.auth.dto;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SignInRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	@DisplayName("유효성 검증: 통과")
	void validate_Normal_Success() {
		//given
		SignInRequest req = new SignInRequest("email@email.com", "!q2w3e4r");
		//when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isEmpty();
	}

	@Test
	@DisplayName("유효성 검증: 실패, null 이메일 ")
	void validate_NullEmail_Fail() {
		//given
		SignInRequest req = new SignInRequest(null, "!q2w3e4r");
		//when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, blank 이메일")
	void validate_BlankEmail_Fail() {
		//given
		SignInRequest req = new SignInRequest("", "!q2w3e4r");
		//when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("");
	}

	@Test
	@DisplayName("유효성 검증: 실패, 이메일 형식 아님")
	void validate_NotFormattedEmail_Fail() {
		//given
		SignInRequest req = new SignInRequest("email", "!q2w3e4r");
		//when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("email");
	}

	@Test
	@DisplayName("유효성 검증: 실패, null 비밀번호")
	void validate_NullPassword_Fail() {
		//given
		SignInRequest req = new SignInRequest("email@email.com", null);
		//when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, blank 비밀번호")
	void validate_BlankPassword_Fail() {
		//given
		SignInRequest req = new SignInRequest("email@email.com", "");
		//when
		Set<ConstraintViolation<SignInRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isNotEmpty();
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("");
	}
}