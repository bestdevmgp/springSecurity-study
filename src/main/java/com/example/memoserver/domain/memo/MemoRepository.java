package com.example.memoserver.domain.memo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<MemoEntity, Long> {
    List<MemoEntity> findByUser_username(String username);
}
