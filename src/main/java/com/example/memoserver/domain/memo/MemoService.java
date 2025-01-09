package com.example.memoserver.domain.memo;

import com.example.memoserver.domain.memo.dto.ResponseGetMemo;
import com.example.memoserver.domain.memo.dto.ResponseGetMemoPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;

    public ResponseGetMemoPage getMemos(int page, String username) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, 10, sort);

        Page<MemoEntity> result = memoRepository.findByUser_username(username, pageable);

        ResponseGetMemoPage responseGetMemoPage = new ResponseGetMemoPage();
        responseGetMemoPage.setTotalPages(result.getTotalPages());
        responseGetMemoPage.setTotalPages(result.getNumber());
        responseGetMemoPage.setSize(10);
        responseGetMemoPage.setContent(result.getContent()
                .stream()
                .map(ResponseGetMemo::new)
                .toList());

        return responseGetMemoPage;
    }

}
