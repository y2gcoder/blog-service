package com.y2gcoder.blog.service.comment.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CommentUpdateRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	@DisplayName("유효성 검증: 성공")
	void validate_Normal_Success() {
		//given
		CommentUpdateRequest req = new CommentUpdateRequest("댓글");
		//when
		Set<ConstraintViolation<CommentUpdateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isEmpty();
	}

	@Test
	@DisplayName("유효성 검증: 실패, empty content")
	void validate_EmptyContent_Fail() {
		//given
		CommentUpdateRequest req = new CommentUpdateRequest(null);
		//when
		Set<ConstraintViolation<CommentUpdateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, blank content")
	void validate_BlankContent_Fail() {
		//given
		CommentUpdateRequest req = new CommentUpdateRequest("");
		//when
		Set<ConstraintViolation<CommentUpdateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains("");
	}
}