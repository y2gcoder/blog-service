package com.y2gcoder.blog.web.controller.auth;

import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import com.y2gcoder.blog.web.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static com.y2gcoder.blog.web.response.ApiResponse.success;

@Api(value = "Auth Controller", tags = "Auth")
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
	private final AuthService authService;

	@ApiOperation(value = "회원가입", notes = "회원가입")
	@PostMapping("/sign-up")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse signUp(@Valid @RequestBody SignUpRequest req) {
		authService.signUp(req);
		return success();
	}

	@ApiOperation(value = "로그인", notes = "로그인")
	@PostMapping("/sign-in")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse signIn(@Valid @RequestBody SignInRequest req) {
		return success(authService.signIn(req));
	}

	@ApiOperation(value = "액세스 토큰 재발급", notes = "리프레시 토큰으로 액세스 토큰을 재발급한다.")
	@PostMapping("/token-refresh")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse tokenRefresh(@ApiIgnore @RequestHeader(value = "Authorization") String refreshToken) {
		return success(authService.refreshAccessToken(refreshToken));
	}

}
