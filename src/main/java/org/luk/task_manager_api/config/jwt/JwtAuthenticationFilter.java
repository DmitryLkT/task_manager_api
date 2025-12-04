package org.luk.task_manager_api.config.jwt;

import org.luk.task_manager_api.config.jwt.JwtService;
import org.luk.task_manager_api.config.CustomUserDetails;
import org.luk.task_manager_api.config.CustomUserDetailsService;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final CustomUserDetailsService customUserDetailsService;

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
                                  @NotNull HttpServletResponse response,
                                  @NotNull FilterChain filterChain) throws ServletException, IOException {
    String token = getTokenFromRequest(request);
    if(token != null && jwtService.isTokenValid(token)) {
      setCustomDetailsToSecurityContextHolder(token);
    }
    filterChain.doFilter(request, response);
  }

  private void setCustomDetailsToSecurityContextHolder(String token) {
    String email = jwtService.extractEmail(token);
    CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(email);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
                  null, customUserDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

    if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}