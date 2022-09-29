package com.y2gcoder.blog.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiSuccess<T> implements ApiResult {
	private T data;
}
