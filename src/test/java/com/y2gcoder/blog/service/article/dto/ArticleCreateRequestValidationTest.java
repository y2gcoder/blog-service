package com.y2gcoder.blog.service.article.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleCreateRequestValidationTest {
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	@DisplayName("유효성 검증: 성공")
	void validate_Normal_Success() {
		//given
		ArticleCreateRequest req = new ArticleCreateRequest(
				"my title",
				"my content",
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80",
				1L,
				null
		);
		//when
		Set<ConstraintViolation<ArticleCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate).isEmpty();
	}

	@Test
	@DisplayName("유효성 검증: 실패, empty 타이틀")
	void validate_EmptyTitle_Fail() {
		//given
		ArticleCreateRequest req = new ArticleCreateRequest(
				null,
				"my content",
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80",
				1L,
				null
		);
		//when
		Set<ConstraintViolation<ArticleCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, blank 타이틀")
	void validate_BlankTitle_Fail() {
		//given
		ArticleCreateRequest req = new ArticleCreateRequest(
				"",
				"my content",
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80",
				1L,
				null
		);
		//when
		Set<ConstraintViolation<ArticleCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains("");
	}

	@Test
	@DisplayName("유효성 검증: 실패, empty content")
	void validate_EmptyContent_Fail() {
		//given
		ArticleCreateRequest req = new ArticleCreateRequest(
				"my title",
				null,
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80",
				1L,
				null
		);
		//when
		Set<ConstraintViolation<ArticleCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, blank content")
	void validate_BlankContent_Fail() {
		//given
		ArticleCreateRequest req = new ArticleCreateRequest(
				"my title",
				"",
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80",
				1L,
				null
		);
		//when
		Set<ConstraintViolation<ArticleCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains("");
	}

	@Test
	@DisplayName("유효성 검증: 실패, empty 카테고리 ID")
	void validate_EmptyCategoryId_Fail() {
		//given
		ArticleCreateRequest req = new ArticleCreateRequest(
				"my title",
				"my content",
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80",
				null,
				null
		);
		//when
		Set<ConstraintViolation<ArticleCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.containsNull();
	}

	@Test
	@DisplayName("유효성 검증: 실패, 음수 카테고리 ID")
	void validate_NegativeCategoryId_Fail() {
		//given
		ArticleCreateRequest req = new ArticleCreateRequest(
				"my title",
				"my content",
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80",
				-1L,
				null
		);
		//when
		Set<ConstraintViolation<ArticleCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(-1L);
	}

	@Test
	@DisplayName("유효성 검증: 실패, 0 카테고리 ID")
	void validate_ZeroCategoryId_Fail() {
		//given
		ArticleCreateRequest req = new ArticleCreateRequest(
				"my title",
				"my content",
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80",
				0L,
				null
		);
		//when
		Set<ConstraintViolation<ArticleCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(0L);
	}

	@Test
	@DisplayName("유효성 검증: 실패, Not Null 멤버 ID")
	void validate_NotNullMemberId_Fail() {
		//given
		ArticleCreateRequest req = new ArticleCreateRequest(
				"my title",
				"my content",
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80",
				1L,
				1L
		);
		//when
		Set<ConstraintViolation<ArticleCreateRequest>> validate = validator.validate(req);
		//then
		assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toSet()))
				.contains(1L);
	}



}