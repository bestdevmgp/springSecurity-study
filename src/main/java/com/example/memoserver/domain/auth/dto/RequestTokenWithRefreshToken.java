package com.example.memoserver.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTokenWithRefreshToken {

    private String refreshToken;
}
