package com.y2gcoder.blog.handler;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JwtHandlerTest {
	JwtHandler jwtHandler = new JwtHandler();
//	String validKey = "testkeytestkeytestkeytestkeytestkeytestkeytestkeytestkey";
	String validKey = "dGVzdGtleXRlc3RrZXl0ZXN0a2V5dGVzdGtleXRlc3RrZXl0ZXN0a2V5dGVzdGtleXRlc3RrZXkK";

	@Test
	@DisplayName("Token: 생성 성공")
	void createToken_Normal_Success() {
		//given
		//when
		String token = jwtHandler.createToken(validKey, Map.of(), 60L);
		//then
		assertThat(token).contains("Bearer ");
	}

	@Test
	@DisplayName("Token: parse 성공")
	void parse_Normal_Success() {
		//given
		String value = "value";
		String token = jwtHandler.createToken(validKey, Map.of(validKey, value), 60L);
		//when
		Claims claims = jwtHandler.parse(validKey, token).orElseThrow(RuntimeException::new);
		//then
		assertThat(claims.get(validKey)).isEqualTo(value);
	}

	@Test
	@DisplayName("Token: parse 실패 Invalid 토큰")
	void parse_InvalidKey_Fail() {
		//given
		String token = jwtHandler.createToken(validKey, Map.of(), 60L);
		//when
		Optional<Claims> claims = jwtHandler.parse("invalidKey", token);
		//then
		assertThat(claims).isEmpty();
	}

	@Test
	@DisplayName("Token: parse 실패 만료된 토큰")
	void parse_ExpiredToken_Fail() {
		//given
		String token = jwtHandler.createToken(validKey, Map.of(), 0L);
		//when
		Optional<Claims> claims = jwtHandler.parse(validKey, token);
		//then
		assertThat(claims).isEmpty();
	}
}