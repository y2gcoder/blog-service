package com.y2gcoder.blog.web.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldErrorDetail {
	private String field;
	private String code;
	private String message;

	public static FieldErrorDetail create(FieldError fieldError) {
		return new FieldErrorDetail(
				fieldError.getField(),
				fieldError.getCode(),
				fieldError.getDefaultMessage()
		);
	}
}
