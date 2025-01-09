package com.example.memoserver.domain.user;

import com.example.memoserver.domain.user.dto.RequestAddUser;
import com.example.memoserver.domain.user.dto.ResponseAddUser;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseAddUser addUser(RequestAddUser requestAddUser) {
        UserEntity user = UserEntity.builder()
                .username(requestAddUser.getUsername())
                .password(passwordEncoder.encode(requestAddUser.getPassword()))
                .build();

        return new ResponseAddUser(userRepository.save(user));
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
