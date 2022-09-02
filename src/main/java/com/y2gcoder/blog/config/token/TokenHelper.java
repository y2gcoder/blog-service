package com.y2gcoder.blog.config.token;

import com.y2gcoder.blog.handler.JwtHandler;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenHelper {
	private final JwtHandler jwtHandler;
	private final String secret;
	private final long expirySeconds;

	private static final String ROLE_TYPE = "ROLE_TYPE";
	private static final String USER_ID = "USER_ID";

	public String createToken(PrivateClaims privateClaims) {
		return jwtHandler.createToken(
			secret,
			Map.of(
					USER_ID, privateClaims.getUserId(),
					ROLE_TYPE, privateClaims.getRoleType()
			),
			expirySeconds
		);
	}

	public Optional<PrivateClaims> parse(String token) {
		return jwtHandler.parse(secret, token).map(this::convert);
	}

	private PrivateClaims convert(Claims claims) {
		return new PrivateClaims(
				claims.get(USER_ID, String.class),
				claims.get(ROLE_TYPE, String.class)
		);
	}

	@Getter
	@AllArgsConstructor
	public static class PrivateClaims {
		private String userId;
		private String roleType;
	}
}
