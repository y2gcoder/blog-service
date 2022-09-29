package com.y2gcoder.blog.api.controller.article.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleReadAllRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	@DisplayName("유효성 검증: 성공")
	void validate_Normal_Success() {
		//given
		ArticleReadAllRequest req = new ArticleReadAllRequest(
				10,
				null,
				null,
				null
		);
		//when
		Set<ConstraintViolation<ArticleReadAllRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isEmpty();
	}

	@Test
	@DisplayName("유효성 검증: 실패, Null Size")
	void validate_NullSize_Fail() {
		//given
		ArticleReadAllRequest req = new ArticleReadAllRequest(
				null,
				null,
				null,
				null
		);
		//when
		Set<ConstraintViolation<ArticleReadAllRequest>> validate = validator.validate(req);
		//then
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue)
						.collect(Collectors.toSet())
		).containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, size 0")
	void validate_ZeroSize_Fail() {
		//given
		ArticleReadAllRequest req = new ArticleReadAllRequest(
				0,
				null,
				null,
				null
		);
		//when
		Set<ConstraintViolation<ArticleReadAllRequest>> validate = validator.validate(req);
		//then
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue)
						.collect(Collectors.toSet())
		).contains(0);
	}

	@Test
	@DisplayName("유효성 검증: 실패, 음수 사이즈")
	void validate_NegativeSize_Fail() {
		//given
		ArticleReadAllRequest req = new ArticleReadAllRequest(
				-1,
				null,
				null,
				null
		);
		//when
		Set<ConstraintViolation<ArticleReadAllRequest>> validate = validator.validate(req);
		//then
		assertThat(
				validate.stream().map(ConstraintViolation::getInvalidValue)
						.collect(Collectors.toSet())
		).contains(-1);
	}
}