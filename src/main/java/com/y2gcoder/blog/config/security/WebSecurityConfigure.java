package com.y2gcoder.blog.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfigure {

	private final CustomUserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return ((request, response, accessDeniedException) -> {
			log.warn("accessDeniedHandler");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		});
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return ((request, response, authException) ->
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
		);
	}

	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(userDetailsService);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
				.antMatchers(HttpMethod.POST, "/api/auth/sign-up", "/api/auth/sign-in", "/api/auth/token-refresh").permitAll()
				.antMatchers(HttpMethod.GET, "/api/**").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
				.antMatchers(HttpMethod.DELETE, "/api/members/{id}").authenticated()
				.antMatchers(HttpMethod.POST, "/api/categories").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/api/categories/{id}").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/api/articles").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/api/articles/{id}").hasRole("ADMIN")
				.antMatchers(HttpMethod.PATCH, "/api/articles/{id}").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/api/comments").authenticated()
				.antMatchers(HttpMethod.DELETE, "/api/comments/{id}").authenticated()
				.antMatchers(HttpMethod.PATCH, "/api/comments/{id}").authenticated()
				.anyRequest().hasAnyRole("ADMIN")
//				.anyRequest().hasAnyRole("ADMIN")
				.and()
				.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler())
				.authenticationEntryPoint(authenticationEntryPoint())
				.and()
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
