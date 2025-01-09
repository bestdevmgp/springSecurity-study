package com.example.memoserver.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Optional 1개 이하의 항목을 받을 때 사용
    // unique = true 일 때 사용
    Optional<UserEntity> findByUsername(String username);
}
