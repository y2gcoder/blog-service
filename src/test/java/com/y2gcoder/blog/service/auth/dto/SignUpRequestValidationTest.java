package com.y2gcoder.blog.service.auth.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SignUpRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	@DisplayName("유효성 검증: 통과")
	void validate_Normal_Success() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "!q2w3e4r");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isEmpty();
	}

	@Test
	@DisplayName("유효성 검증: 실패, null 이메일")
	void validate_NullEmail_Fail() {
		//given
		SignUpRequest req = new SignUpRequest(null, "!q2w3e4r");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, blank 이메일")
	void validate_BlankEmail_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("", "!q2w3e4r");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("");
	}

	@Test
	@DisplayName("유효성 검증: 실패, 이메일 형식 아님")
	void validate_NotFormattedEmail_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("email", "!q2w3e4r");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("email");
	}

	@Test
	@DisplayName("유효성 검증: 실패, null 비밀번호")
	void validate_NullPassword_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", null);
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, blank 비밀번호")
	void validate_BlankPassword_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("");
	}

	@Test
	@DisplayName("유효성 검증: 실패, 7자리 비밀번호")
	void validate_Size7Password_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "!q2w3e4");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("!q2w3e4");
	}
	
	@Test
	@DisplayName("유효성 검증: 실패, 비밀번호 특수문자 없음")
	void validate_NotExistsSpecialSymbols_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "1q2w3e4");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("1q2w3e4");
	}

	@Test
	@DisplayName("유효성 검증: 실패, 비밀번호 숫자 없음")
	void validate_NotExistsNumber_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "!abcdefg");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("!abcdefg");
	}

	@Test
	@DisplayName("유효성 검증: 실패, 비밀번호 영어 알파벳 없음")
	void validate_NotExistsAlphabets_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "!1234567");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("!1234567");
	}

	@Test
	@DisplayName("유효성 검증: 실패, 비밀번호 특수문자만 존재함")
	void validate_OnlySpecialSymbols_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "!@#$%&*?");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("!@#$%&*?");
	}

	@Test
	@DisplayName("유효성 검증: 실패, 비밀번호 숫자만 존재함")
	void validate_OnlyNumbers_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "1234578");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("1234578");
	}

	@Test
	@DisplayName("유효성 검증: 실패, 비밀번호 영어 알파벳만 존재함")
	void validate_OnlyAlphabets_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "abcdefgh");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("abcdefgh");
	}

	@Test
	@DisplayName("유효성 검증: 실패, 비밀번호에 허용하지 않는 특수문자 포함")
	void validate_NotAllowedSpecialSymbols_Fail() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "^q2w3e4r");
		//when
		Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("^q2w3e4r");
	}
}