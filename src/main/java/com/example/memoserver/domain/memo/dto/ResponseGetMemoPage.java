package com.example.memoserver.domain.memo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseGetMemoPage {
    private int totalPages;

    private int page;

    private int size;

    private List<ResponseGetMemo> content;
}
