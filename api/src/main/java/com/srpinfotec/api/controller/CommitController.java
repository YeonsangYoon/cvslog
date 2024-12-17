package com.srpinfotec.api.controller;

import com.srpinfotec.api.dto.ApiResponse;
import com.srpinfotec.api.dto.request.CommitRqCond;
import com.srpinfotec.api.dto.response.CommitRsDto;
import com.srpinfotec.api.dto.response.RevisionRsDto;
import com.srpinfotec.api.service.CommitService;
import com.srpinfotec.api.service.RevisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommitController {
    private final CommitService commitService;
    private final RevisionService revisionService;

    /**
     * Commit 리스트 조회
     */
    @GetMapping("/commit")
    public ResponseEntity<ApiResponse> commitListByProject(@ModelAttribute CommitRqCond cond){
        List<CommitRsDto> commits = commitService.getCommitList(cond);

        return ResponseEntity
                .ok(ApiResponse.success(commits));
    }

    /**
     * 최근 Commit 로그 조회
     */
    @GetMapping("/commit/recent")
    public ResponseEntity<ApiResponse> recentFetchedCommit(){
        List<CommitRsDto> recentCommit = commitService.getCommitList(
                CommitRqCond.builder()
                        .startDate(LocalDate.now())
                        .build()
        );

        Map<String, List<CommitRsDto>> projectCommit = recentCommit.stream().collect(Collectors.groupingBy(CommitRsDto::getProjectName));

        return ResponseEntity
                .ok(ApiResponse.success(projectCommit));
    }

    @GetMapping("/commit/{commitId}/revision")
    public ResponseEntity<ApiResponse> revisionListByCommitId(@PathVariable Long commitId){
        List<RevisionRsDto> revisions = revisionService.getRevisionByCommit(commitId);

        return ResponseEntity
                .ok(ApiResponse.success(revisions));
    }
}
