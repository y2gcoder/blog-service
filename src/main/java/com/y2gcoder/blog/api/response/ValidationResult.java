package com.y2gcoder.blog.api.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationResult implements ApiResult{
	List<FieldErrorDetail> errors;

	public static ValidationResult create(Errors errors) {
		List<FieldErrorDetail> details = errors.getFieldErrors()
				.stream()
				.map(FieldErrorDetail::create)
				.collect(Collectors.toList());
		return new ValidationResult(details);
	}
}
