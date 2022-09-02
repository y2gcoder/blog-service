package com.y2gcoder.blog.config.token;

import com.y2gcoder.blog.handler.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TokenHelperTest {
	TokenHelper tokenHelper;
	//	String validKey = "testkeytestkeytestkeytestkeytestkeytestkeytestkeytestkey";
	String validKey = "dGVzdGtleXRlc3RrZXl0ZXN0a2V5dGVzdGtleXRlc3RrZXl0ZXN0a2V5dGVzdGtleXRlc3RrZXkK";

	@BeforeEach
	void beforeEach() {
		tokenHelper = new TokenHelper(new JwtHandler(), validKey, 1000L);
	}

	@Test
	@DisplayName("Token: createToken, Parse 성공")
	void createToken_parse_Normal_Success() {
		//given
		String userId = "1";
		String roleType = "USER";
		TokenHelper.PrivateClaims privateClaims = new TokenHelper.PrivateClaims(userId, roleType);

		//when
		String token = tokenHelper.createToken(privateClaims);
		TokenHelper.PrivateClaims parsedPrivateClaims = tokenHelper.parse(token).orElseThrow(RuntimeException::new);

		//then
		assertThat(parsedPrivateClaims.getUserId()).isEqualTo(userId);
		assertThat(parsedPrivateClaims.getRoleType()).isEqualTo(roleType);
	}
}