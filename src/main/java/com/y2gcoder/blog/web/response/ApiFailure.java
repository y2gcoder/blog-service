package com.y2gcoder.blog.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiFailure implements ApiResult {
	private String msg;
}
