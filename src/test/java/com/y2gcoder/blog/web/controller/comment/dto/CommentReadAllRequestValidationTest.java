package com.y2gcoder.blog.web.controller.comment.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CommentReadAllRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	@DisplayName("유효성 검증: 성공")
	void validate_Normal_Success() {
		//given
		CommentReadAllRequest req = new CommentReadAllRequest(
				10,
				null,
				null
		);
		//when
		Set<ConstraintViolation<CommentReadAllRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isEmpty();
	}

	@Test
	@DisplayName("유효성 검증: 실패, Empty Size")
	void validate_EmptySize_Fail() {
		CommentReadAllRequest req = new CommentReadAllRequest(
				null,
				null,
				null
		);
		//when
		Set<ConstraintViolation<CommentReadAllRequest>> validate = validator.validate(req);
		//then
		assertThat(
			validate.stream().map(ConstraintViolation::getInvalidValue)
					.collect(Collectors.toSet())
		).containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, Size 0")
	void validate_ZeroSize_Fail() {
		CommentReadAllRequest req = new CommentReadAllRequest(
				0,
				null,
				null
		);
		//when
		Set<ConstraintViolation<CommentReadAllRequest>> validate = validator.validate(req);
		//then
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue)
						.collect(Collectors.toSet())
		).contains(0);
	}

	@Test
	@DisplayName("유효성 검증: 실패, Size 음수")
	void validate_NegativeSize_Fail() {
		CommentReadAllRequest req = new CommentReadAllRequest(
				-1,
				null,
				null
		);
		//when
		Set<ConstraintViolation<CommentReadAllRequest>> validate = validator.validate(req);
		//then
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue)
						.collect(Collectors.toSet())
		).contains(-1);
	}
}