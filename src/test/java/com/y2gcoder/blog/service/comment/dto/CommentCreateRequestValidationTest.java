package com.y2gcoder.blog.service.comment.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CommentCreateRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


	@Test
	@DisplayName("유효성 검증: 성공")
	void validate_Normal_Success() {
		//given
		CommentCreateRequest req = new CommentCreateRequest(
			"content",
				null,
				1L
		);
		//when
		Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isEmpty();
	}

	@Test
	@DisplayName("유효성 검증: 실패, empty content")
	void validate_EmptyContent_Fail() {
		//given
		CommentCreateRequest req = new CommentCreateRequest(
				null,
				null,
				1L
		);
		//when
		Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, blank content")
	void validate_BlankContent_Fail() {
		//given
		CommentCreateRequest req = new CommentCreateRequest(
				"",
				null,
				1L
		);
		//when
		Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains("");
	}

	@Test
	@DisplayName("유효성 검증: 실패, Not Null 회원 ID")
	void validate_NotNullMemberId_Fail() {
		//given
		CommentCreateRequest req = new CommentCreateRequest(
				"content",
				1L,
				1L
		);
		//when
		Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(1L);
	}

	@Test
	@DisplayName("유효성 검증: 실패, Empty 게시글 ID")
	void validate_EmptyArticleId_Fail() {
		//given
		CommentCreateRequest req = new CommentCreateRequest(
				"content",
				null,
				null
		);
		//when
		Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, 음수 게시글 ID")
	void validate_NegativeArticleId_Fail() {
		//given
		CommentCreateRequest req = new CommentCreateRequest(
				"content",
				null,
				-1L
		);
		//when
		Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(-1L);
	}

	@Test
	@DisplayName("유효성 검증: 실패, 0 게시글 ID")
	void validate_ZeroArticleId_Fail() {
		//given
		CommentCreateRequest req = new CommentCreateRequest(
				"content",
				null,
				0L
		);
		//when
		Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(0L);
	}
}