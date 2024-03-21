package com.swp.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.swp.server.enums.AccountRole;
import com.swp.server.services.jwt.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	private static final String[] PUBLIC_URL = { "/api/v1/auth/**", "/v3/api-docs/**", "/v3/api-docs.yaml",
			"/swagger-ui/**", "/swagger-ui.html", "/chat", "/product", "/product/**", "/api/chatbox/**",
			"/update-cv/**", "/api/test/**", "/api/profile/**", "/api/favorite/**", "/api/notify/**", "/api/job/**",
			"/api/jobApply/**", "/getMessages", "/getMessages/**", "/chat-socket/**", "/chat/**", "/createOrGetChat/**",
			"/getMessages/**" };

	@Autowired
	private UserService userService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(request -> request.requestMatchers("/api/auth/**").permitAll()
//						.requestMatchers("/api/auth/profile")
//						.hasAnyAuthority(AccountRole.ADMIN.name(), AccountRole.EMPLOYER.name(),
//								AccountRole.JOBSEEKER.name(), AccountRole.SUPPORTER.name())
						.requestMatchers(PUBLIC_URL).permitAll().requestMatchers("/api/admin/**")
						.hasAnyAuthority(AccountRole.ADMIN.name()).anyRequest().authenticated())
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userService.UserDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
