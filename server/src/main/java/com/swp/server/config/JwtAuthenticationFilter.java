package com.swp.server.config;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.swp.server.entities.Account;
import com.swp.server.repository.AccountRepo;
import com.swp.server.services.jwt.UserService;
import com.swp.server.utils.JwtUtil;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private AccountRepo accountRepo;

	private final JwtUtil jwtService;

	private final UserService userService;

	public JwtAuthenticationFilter(JwtUtil jwtService, UserService userService) {
		super();
		this.jwtService = jwtService;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response,
			@Nonnull FilterChain filterChain) throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		String jwt;

		if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);

		String userEmail = jwtService.extractUserName(jwt);

		if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userService.UserDetailsService().loadUserByUsername(userEmail);
			Optional<Account> account = accountRepo.findFirstByUsername(userDetails.getUsername());
			System.out.println(jwtService.isTokenValid(jwt, account.get().getEmail()));

			if (jwtService.isTokenValid(jwt, account.get().getEmail()) == true) {
//				System.out.println(jwtService.isTokenValid(jwt, userDetails));
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				context.setAuthentication(authenticationToken);
				SecurityContextHolder.setContext(context);
				System.out.println(context);
			}
		}
		filterChain.doFilter(request, response);
	}

}
