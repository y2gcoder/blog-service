package com.y2gcoder.blog.web.auth;

import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import com.y2gcoder.blog.web.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.y2gcoder.blog.web.response.ApiResponse.success;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
	private final AuthService authService;

	@PostMapping("/sign-up")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse signUp(@Valid @RequestBody SignUpRequest req) {
		authService.signUp(req);
		return success();
	}

	@PostMapping("/sign-in")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse signIn(@Valid @RequestBody SignInRequest req) {
		return success(authService.signIn(req));
	}

	@PostMapping("/token-refresh")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse tokenRefresh(@RequestHeader(value = "Authorization") String refreshToken) {
		return success(authService.refreshAccessToken(refreshToken));
	}

}
