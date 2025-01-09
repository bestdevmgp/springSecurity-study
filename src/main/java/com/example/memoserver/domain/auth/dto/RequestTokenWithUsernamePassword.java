package com.example.memoserver.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTokenWithUsernamePassword {
    private String username;
    private String password;
}
