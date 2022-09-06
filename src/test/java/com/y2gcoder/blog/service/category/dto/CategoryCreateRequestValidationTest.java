package com.y2gcoder.blog.service.category.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryCreateRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	@DisplayName("유효성 검증: 통과")
	void validate_Normal_Success() {
		//given
		CategoryCreateRequest req = new CategoryCreateRequest("category");
		//when
		Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isEmpty();
	}

	@Test
	@DisplayName("유효성 검증: 실패, null 카테고리명")
	void validate_NullName_Fail() {
		//given
		CategoryCreateRequest req = new CategoryCreateRequest(null);
		//when
		Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, blank 카테고리명")
	void validate_BlankName_Fail() {
		//given
		CategoryCreateRequest req = new CategoryCreateRequest("");
		//when
		Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet())).contains("");
	}
}