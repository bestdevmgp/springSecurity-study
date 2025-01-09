package com.example.memoserver.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAddUser {

    private String username;
    private String password;
}
