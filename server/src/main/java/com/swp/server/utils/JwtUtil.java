package com.swp.server.utils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String SIGNING_KEY = "aLcGQFJB8HdvX1Vj7Imy9wP5gN2bW3EYs6qoKDtuZfTh0k4RxSaMzniUOrpCe";

	private Key getSigninKey() {
		byte[] apiKeySecretBytes = Base64.getEncoder().encode(SIGNING_KEY.getBytes());
		return Keys.hmacShaKeyFor(apiKeySecretBytes);
	}

	public String generateToken(String email, boolean remember) {
		long time = 0;
		if (remember == true) {
			time = (24 * 60 * 60 * 1000); // one day
		} else if (remember == false) {
			time = 3 * (24 * 60 * 60 * 1000); // 3 day
		}
		return generateToken(new HashMap<>(), email, time);
	}

	private String generateToken(Map<String, Object> extraClaims, String email, long time) {
		return Jwts.builder().setClaims(extraClaims).setSubject(email).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + time))
				.signWith(getSigninKey(), SignatureAlgorithm.HS256).compact();
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
		final Claims claims = extractAllClaims(token);
		return claimsResolvers.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody();
	}

	//
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	//
	public boolean isTokenValid(String token, String email) {
		String emailExtract = extractUserName(token);
		System.out.println(email);
		System.out.println(emailExtract);
		return (emailExtract.equals(email) && !isTokenExpired(token));
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}
}
