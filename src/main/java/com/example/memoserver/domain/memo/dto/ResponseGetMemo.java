package com.example.memoserver.domain.memo.dto;

import com.example.memoserver.domain.memo.MemoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ResponseGetMemo {

    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private Long userId;

    public ResponseGetMemo(MemoEntity memoEntity) {
        this.id = memoEntity.getId();
        this.content = memoEntity.getContent();
        this.createdAt = memoEntity.getCreatedAt();
        this.userId = memoEntity.getUser().getId();
    }
}
