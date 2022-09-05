package com.y2gcoder.blog.service.auth;

public class SignInFailureException extends IllegalArgumentException {
	public SignInFailureException() {
		super();
	}

	public SignInFailureException(String s) {
		super(s);
	}
}
