package com.y2gcoder.blog.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtHandler {
	private static final String TYPE = "Bearer ";

	public String createToken(String secret, Map<String, Object> privateClaims, long expirySeconds) {
		Date now = new Date();
		Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		return TYPE + Jwts.builder()
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + expirySeconds * 1000L))
				.addClaims(privateClaims)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public Optional<Claims> parse(String secret, String token) {
		try {
			Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
			return Optional.of(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(untype(token)).getBody());
		} catch (JwtException e) {
			return Optional.empty();
		}
	}

	private String untype(String token) {
		return token.substring(TYPE.length());
	}

}
