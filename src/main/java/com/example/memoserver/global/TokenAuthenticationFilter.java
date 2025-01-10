package com.example.memoserver.global;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = getHeaderValue(request);
        if (token != null) {
            try {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                setErrorResponse(response,e);
            }
        }

        filterChain.doFilter(request, response); //토큰이 없거나 예외가 발생한 경우 filterChain.doFilter()가 호출되지 않아 요청이 체인의 다음 단계로 전달되지 않았
    }

    private String getHeaderValue(HttpServletRequest request) {
        String headerValue = request.getHeader("Authorization");
        String prefix = "Bearer ";
        if (headerValue != null && headerValue.startsWith(prefix)) {
            return headerValue.substring(prefix.length());
        } else {
            return null;
        }
    }

    public void setErrorResponse(HttpServletResponse response, Throwable throwable) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("aplication/json;charset=utf-8");

        if (throwable instanceof ExpiredJwtException) {
            response.setStatus(419);
            response.getWriter().write(throwable.getMessage());
        }
    }
}