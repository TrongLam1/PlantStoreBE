package com.java.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.java.service.impl.JwtServiceImpl;
import com.java.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtServiceImpl jwtServiceImpl;

	private final UserServiceImpl userServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;

		if (org.apache.commons.lang.StringUtils.isEmpty(authHeader) || !org.apache.commons.lang.StringUtils.startsWith(authHeader, "Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);
		userEmail = jwtServiceImpl.extractUsername(jwt);

		if (org.apache.commons.lang.StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userServiceImpl.userDetailsService().loadUserByUsername(userEmail);

			if (jwtServiceImpl.isTokenValid(jwt, userDetails)) {
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());
				
				token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				securityContext.setAuthentication(token);
				SecurityContextHolder.setContext(securityContext);
			}
		}
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		filterChain.doFilter(request, response);
	}
}
