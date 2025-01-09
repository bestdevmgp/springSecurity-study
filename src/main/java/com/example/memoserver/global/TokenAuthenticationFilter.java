package com.example.memoserver.global;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
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

    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getHeaderValue(request);
        if (token != null) {
            try {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (IllegalArgumentException e) {

            } catch (ExpiredJwtException e) {

            } catch (SignatureException e) {

            } catch (Exception e) {

            }
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private String getHeaderValue(HttpServletRequest request) {
        String headerValue = request.getHeader("Authorization");
        String prefix = "Bearer ";

        if (headerValue != null && headerValue.startsWith(prefix)) {
            return headerValue.substring(prefix.length());
        } else
            return null;
    }

    public void setErrorResponse(HttpServletResponse response, Throwable throwable) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");

        if (throwable instanceof ExpiredJwtException) {
            response.setStatus(419);
            response.getWriter().write(throwable.getMessage());
        }
    }
}