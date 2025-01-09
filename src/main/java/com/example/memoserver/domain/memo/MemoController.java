package com.example.memoserver.domain.memo;

import com.example.memoserver.domain.memo.dto.ResponseGetMemoPage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memos")
public class MemoController {
    private final MemoService memoService;

    @GetMapping
    public ResponseEntity<ResponseGetMemoPage> getMemos(
            @RequestParam(name = "page", defaultValue = "1") int page,
            Principal principal
    ) {
        principal.getName();
        return ResponseEntity.ok(memoService.getMemos(page));
    }
}