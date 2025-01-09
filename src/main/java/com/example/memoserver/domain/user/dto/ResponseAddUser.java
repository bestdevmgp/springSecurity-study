package com.example.memoserver.domain.user.dto;

import com.example.memoserver.domain.user.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAddUser {
    private String username;

    public ResponseAddUser(UserEntity user) {
        this.username = user.getUsername();
    }
}
