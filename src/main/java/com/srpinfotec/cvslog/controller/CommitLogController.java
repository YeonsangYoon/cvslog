package com.srpinfotec.cvslog.controller;

import com.srpinfotec.cvslog.service.CVSService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommitLogController {
    private final CVSService cvsService;

    @GetMapping("/commit/recent")
    public void recentCommit(){
        cvsService.updateHistory();
    }
}
