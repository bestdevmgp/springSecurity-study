package com.example.memoserver.domain.auth;

import com.example.memoserver.domain.auth.dto.RequestTokenWithRefreshToken;
import com.example.memoserver.domain.auth.dto.RequestTokenWithUsernamePassword;
import com.example.memoserver.domain.auth.dto.ResponseCreateAccessToken;
import com.example.memoserver.domain.user.UserService;
import com.example.memoserver.domain.user.dto.RequestAddUser;
import com.example.memoserver.domain.user.dto.ResponseAddUser;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseAddUser> signup(@RequestBody RequestAddUser requestAddUser) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userService.addUser(requestAddUser));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseCreateAccessToken> signup(@RequestBody RequestTokenWithUsernamePassword request) {
        try {
            ResponseCreateAccessToken token = tokenService.createAccessTokenByUsernamePassword(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/signin/token")
    public ResponseEntity<ResponseCreateAccessToken> signup(@RequestBody RequestTokenWithRefreshToken request) {
        try {
            ResponseCreateAccessToken token = tokenService.createAccessTokenByRefreshToken(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(419).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
