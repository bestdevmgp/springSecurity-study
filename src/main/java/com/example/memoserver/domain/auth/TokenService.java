package com.example.memoserver.domain.auth;

import com.example.memoserver.domain.auth.dto.RequestTokenWithRefreshToken;
import com.example.memoserver.domain.auth.dto.RequestTokenWithUsernamePassword;
import com.example.memoserver.domain.auth.dto.ResponseCreateAccessToken;
import com.example.memoserver.domain.user.UserEntity;
import com.example.memoserver.domain.user.UserService;
import com.example.memoserver.global.TokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseCreateAccessToken createAccessTokenByUsernamePassword(RequestTokenWithUsernamePassword request) {
        UserEntity user = userService.getUserByUsername(request.getUsername());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 비밀번호 비교 로직 BCrypt로 수정할 것
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String accessToken = tokenProvider.createToken(request.getUsername(), true);
        String refreshToken = tokenProvider.createToken(request.getUsername(), false);
        tokenRepository.save(new TokenEntity(user.getUsername(),refreshToken));
        return new ResponseCreateAccessToken(accessToken,refreshToken);
    }

    public ResponseCreateAccessToken createAccessTokenByRefreshToken(RequestTokenWithRefreshToken request) {
        Claims claims = tokenProvider.getClaimsFromToken(request.getRefreshToken());

        if (claims == null || claims.get("type", String.class).equals(TokenProvider.TYPE_REFRESH)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid refresh token");
        }

        UserEntity user = userService.getUserByUsername(claims.getSubject());
        if (user == null) throw new RuntimeException("User not found");

        TokenEntity token = tokenRepository.findById(user.getUsername()).orElse(null);
        if (token != null && !token.getToken().equals(request.getRefreshToken())) throw new RuntimeException("Expired token");

        String accessToken = tokenProvider.createToken(user.getUsername(), true);
        String refreshToken = tokenProvider.createToken(user.getUsername(), false);
        tokenRepository.save(new TokenEntity(user.getUsername(), refreshToken));
        return new ResponseCreateAccessToken(accessToken, refreshToken);
    }
}
